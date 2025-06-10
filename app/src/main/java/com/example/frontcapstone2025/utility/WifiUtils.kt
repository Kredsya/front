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
    val rssi: Double,
    val ssid: String,
    val bssid: String,
    val distance: Double,
    val rawRssi: Double
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
            val rawRssi = res.level.toDouble()
            val ukf = ukfMap.getOrPut(res.BSSID) { WifiUkf(initial = rawRssi) }
            val filteredRssi = ukf.update(rawRssi)
            val dist = rssiToDistance(filteredRssi)
            WifiDisplay(
                filteredRssi,
                res.SSID.ifBlank { res.BSSID },
                res.BSSID,
                dist,
                rawRssi
            )
        }
        .sortedBy { it.distance }

/* ---------- RSSI → 거리 ---------- */
fun rssiToDistance(rssi: Double, walls: Int = 1): Double {
    val totalLoss = (WifiConfig.rssiAt1m.toDouble() - rssi) - (walls * WifiConfig.wallLossDb)
    return 10.0.pow(totalLoss / (10 * WifiConfig.pathLossExponent))
}

/* -------------------------------------------------------------------------- */
/* ---------------------  RSSI Unscented Kalman Filter  --------------------- */
/* -------------------------------------------------------------------------- */

class WifiUkf(
    initial: Double,
    private val eta: Double = WifiConfig.pathLossExponent,
    private val dt: Double = 1.0,
    private val sigmaProcess: Double = 1.0,
    private val alpha: Double = 1.0,
    private val kappa: Double = 0.0,
    private val beta: Double = 2.0,
) {

    private val n = 3
    private val lambda = alpha * alpha * (n + kappa) - n
    private val numSigma = 2 * n + 1
    private val c = n + lambda

    private val wM = DoubleArray(numSigma)
    private val wC = DoubleArray(numSigma)

    var x = doubleArrayOf(initial, 0.0, -40.0)
        private set
    var P = arrayOf(
        doubleArrayOf(25.0, 0.0, 0.0),
        doubleArrayOf(0.0, 4.0, 0.0),
        doubleArrayOf(0.0, 0.0, 25.0)
    )
        private set

    init {
        wM[0] = lambda / c
        wC[0] = wM[0] + 1.0 - alpha * alpha + beta
        for (i in 1 until numSigma) {
            wM[i] = 1.0 / (2.0 * c)
            wC[i] = wM[i]
        }
    }

    fun update(z: Double): Double {
        val sigma = computeSigmaPoints()
        val xPred = DoubleArray(n)
        val sigmaPred = Array(numSigma) { DoubleArray(n) }

        for (i in 0 until numSigma) {
            val rssi = sigma[i][0]
            val v = sigma[i][1]
            val pInit = sigma[i][2]
            val expTerm = 10.0.pow((pInit - rssi) / (10.0 * eta))
            val rssiPrime = rssi - 10.0 * eta * v * expTerm * dt
            sigmaPred[i][0] = rssiPrime
            sigmaPred[i][1] = v
            sigmaPred[i][2] = pInit
            for (j in 0 until n) xPred[j] += wM[i] * sigmaPred[i][j]
        }

        val Py = Array(n) { DoubleArray(n) }
        for (i in 0 until numSigma) {
            val dx = diff(sigmaPred[i], xPred)
            addOuterProduct(Py, dx, dx, wC[i])
        }
        addMatrix(Py, processNoiseCovariance())

        x = xPred
        P = Py

        val zSigma = DoubleArray(numSigma) { sigmaPred[it][0] }
        var zPred = 0.0
        for (i in 0 until numSigma) zPred += wM[i] * zSigma[i]

        var Pzz = 0.0
        for (i in 0 until numSigma) {
            val dz = zSigma[i] - zPred
            Pzz += wC[i] * dz * dz
        }
        val R = measurementNoise(z)
        Pzz += R

        val Pyz = DoubleArray(n)
        for (i in 0 until numSigma) {
            val dx = diff(sigmaPred[i], xPred)
            val dz = zSigma[i] - zPred
            for (j in 0 until n) Pyz[j] += wC[i] * dx[j] * dz
        }

        val K = DoubleArray(n) { Pyz[it] / Pzz }

        val residual = z - zPred
        for (i in 0 until n) x[i] += K[i] * residual
        for (i in 0 until n) {
            for (j in 0 until n) {
                P[i][j] -= K[i] * Pzz * K[j]
            }
        }
        return x[0]
    }

    fun estimatedDistance(): Double = 10.0.pow((x[0] - x[2]) / (10.0 * eta))

    private fun computeSigmaPoints(): Array<DoubleArray> {
        val sqrt = choleskyScaled(P, c)
        val sigma = Array(numSigma) { DoubleArray(n) }
        sigma[0] = x.copyOf()
        for (i in 0 until n) {
            for (j in 0 until n) {
                sigma[1 + i][j] = x[j] + sqrt[j][i]
                sigma[1 + n + i][j] = x[j] - sqrt[j][i]
            }
        }
        return sigma
    }

    private fun choleskyScaled(A: Array<DoubleArray>, scale: Double): Array<DoubleArray> {
        val S = Array(n) { DoubleArray(n) }
        for (i in 0 until n) for (j in 0 until n) S[i][j] = A[i][j] * scale
        val L = Array(n) { DoubleArray(n) }
        for (i in 0 until n) {
            for (j in 0..i) {
                var sum = S[i][j]
                for (k in 0 until j) sum -= L[i][k] * L[j][k]
                if (i == j) {
                    L[i][j] = kotlin.math.sqrt(sum)
                } else {
                    L[i][j] = sum / L[j][j]
                }
            }
        }
        return L
    }

    private fun processNoiseCovariance(): Array<DoubleArray> {
        val q = sigmaProcess * sigmaProcess
        return arrayOf(
            doubleArrayOf(q * dt * dt * dt / 3.0, q * dt * dt / 2.0, 0.0),
            doubleArrayOf(q * dt * dt / 2.0, q * dt, 0.0),
            doubleArrayOf(0.0, 0.0, q)
        )
    }

    private fun measurementNoise(z: Double): Double {
        val term = (3.0 * z + 340.0) / 70.0
        return term * term
    }

    private fun diff(a: DoubleArray, b: DoubleArray): DoubleArray =
        DoubleArray(n) { a[it] - b[it] }

    private fun addOuterProduct(M: Array<DoubleArray>, v1: DoubleArray, v2: DoubleArray, w: Double) {
        for (i in 0 until n) {
            for (j in 0 until n) {
                M[i][j] += w * v1[i] * v2[j]
            }
        }
    }

    private fun addMatrix(dst: Array<DoubleArray>, src: Array<DoubleArray>) {
        for (i in 0 until n) for (j in 0 until n) dst[i][j] += src[i][j]
    }
}

/* ---------- 무향 칼만 필터(zero-phase) ---------- */
fun zeroPhaseUkf(values: List<Double>): List<Double> {
    if (values.isEmpty()) return emptyList()
    val forward = mutableListOf<Double>()
    val ukfForward = WifiUkf(initial = values.first())
    values.forEach { forward.add(ukfForward.update(it)) }

    val backward = mutableListOf<Double>()
    val ukfBackward = WifiUkf(initial = forward.last())
    forward.asReversed().forEach { backward.add(ukfBackward.update(it)) }

    return backward.asReversed()
}

/* ---------- Wifi 설정 값 ---------- */
// WifiConfig 객체의 값을 수정하여 조정 가능

/* -------------------------------------------------------------------------- */
/* --------------------------  Compose 상태 헬퍼  --------------------------- */
/* -------------------------------------------------------------------------- */

@Composable
fun rememberWifiDistances(
    locationGranted: Boolean,
    wifiScanDelay: Long,
    resetKey: Int = 0
): State<List<WifiDisplay>> {
    val context = LocalContext.current
    return produceState(
        initialValue = emptyList(),
        key1 = locationGranted,
        key2 = resetKey
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
