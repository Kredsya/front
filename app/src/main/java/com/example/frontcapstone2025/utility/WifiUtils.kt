package com.example.frontcapstone2025.utility

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.frontcapstone2025.utility.WifiConfig
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.pow

/* -------------------------------------------------------------------------- */
/* ----------------------------  데이터 & 계산  ----------------------------- */
/* -------------------------------------------------------------------------- */

data class WifiDisplay(
    val rssi: Int,
    val ssid: String,
    val bssid: String,
    val distance: Double
) {
    val distanceString: String =
        String.format(Locale.US, "%.2f m", distance)
}

fun List<ScanResult>.toDisplayList(
    ukfMap: MutableMap<String, WifiUkf>
): List<WifiDisplay> =
    this
        .filter { it.level >= WifiConfig.minRssi }                       // 신호 필터링
        .map { res ->
            val raw = rssiToDistance(res.level)
            val ukf = ukfMap.getOrPut(res.BSSID) { WifiUkf(initial = raw) }
            val dist = ukf.update(raw)
            WifiDisplay(
                res.level,
                res.SSID.ifBlank { res.BSSID },
                res.BSSID,
                dist
            )
        }
        .sortedBy { it.distance }

/* ---------- RSSI → 거리 ---------- */
fun rssiToDistance(rssi: Int, walls: Int = 0): Double {
    val totalLoss = (WifiConfig.rssiAt1m - rssi) - (walls * WifiConfig.wallLossDb)
    return 10.0.pow(totalLoss / (10 * WifiConfig.pathLossExponent))
}

/* -------------------------------------------------------------------------- */
/* ---------------------------  UKF 1-차원 구현  ---------------------------- */
/* -------------------------------------------------------------------------- */

class WifiUkf(
    initial: Double,
    private var P: Double = 1.0,
    private val Q: Double = 0.1,
    private val R: Double = 0.5
) {
    private var x = initial
    private val alpha = 1e-3
    private val kappa = 0.0
    private val beta = 2.0

    fun update(z: Double): Double {
        /* ---------- 예측 ---------- */
        val xPred = x
        val PPred = P + Q

        /* ---------- Σ-점 ---------- */
        val n = 1
        val lambda = alpha * alpha * (n + kappa) - n
        val c = n + lambda
        val sqrtTerm = kotlin.math.sqrt(c * PPred)
        val sigma = doubleArrayOf(xPred, xPred + sqrtTerm, xPred - sqrtTerm)

        val wm0 = lambda / c
        val wc0 = wm0 + (1 - alpha * alpha + beta)
        val wi = 1.0 / (2 * c)
        val wm = doubleArrayOf(wm0, wi, wi)
        val wc = doubleArrayOf(wc0, wi, wi)

        var zPred = 0.0
        for (i in sigma.indices) zPred += wm[i] * sigma[i]

        var S = R
        var Cxz = 0.0
        for (i in sigma.indices) {
            val dz = sigma[i] - zPred
            val dx = sigma[i] - xPred
            S += wc[i] * dz * dz
            Cxz += wc[i] * dx * dz
        }

        val K = Cxz / S
        x = xPred + K * (z - zPred)
        P = PPred - K * S * K

        return x
    }
}

/* ---------- Wifi 설정 값 ---------- */
// WifiConfig 객체의 값을 수정하여 조정 가능

/* -------------------------------------------------------------------------- */
/* --------------------------  Compose 상태 헬퍼  --------------------------- */
/* -------------------------------------------------------------------------- */

@Composable
fun rememberWifiDistances(
    locationGranted: Boolean,
    wifiScanDelay: Long
): State<List<WifiDisplay>> {
    val context = LocalContext.current
    return produceState(
        initialValue = emptyList(),
        key1 = locationGranted
    ) {
        if (!locationGranted) {
            value = emptyList()
            return@produceState
        }

        val wifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (!wifiManager.isWifiEnabled) wifiManager.isWifiEnabled = true

        /* UKF 상태를 BSSID별로 보존 */
        val ukfMap: SnapshotStateMap<String, WifiUkf> = mutableStateMapOf()

        /* 브로드캐스트 수신기로 스캔 결과 갱신 */
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val results = wifiManager.scanResults
                    value = results.toDisplayList(ukfMap)
                    // 이후 처리
                } else {
                    // 권한 없으면 요청하거나 무시
                }
//                val results = wifiManager.scanResults
//                value = results.toDisplayList(ukfMap)
            }
        }
        context.registerReceiver(
            receiver,
            IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        )

        /* 지정 주기로 스캔 반복 */
        while (true) {
            try {
                wifiManager.startScan()
            } catch (_: Exception) { /* ignore */
            }
            delay(wifiScanDelay)
        }

        /* onDispose – produceState 자동 해제 */
        @Suppress("UNREACHABLE_CODE")
        context.unregisterReceiver(receiver)
    }
}
