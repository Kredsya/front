package com.example.frontcapstone2025.presentation.screen

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.delay
import com.example.frontcapstone2025.components.buttons.CustomButton
import com.example.frontcapstone2025.components.items.WifiComponent
import com.example.frontcapstone2025.components.layout.BottomMenu
import com.example.frontcapstone2025.components.layout.MainPageTopBar
import com.example.frontcapstone2025.ui.theme.TextColorGray
import kotlin.math.pow
import kotlin.math.sqrt
import java.util.Locale
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateMap

/* -------------------------------------------------------------------------- */
/* ------------------------------  Composable  ------------------------------ */
/* -------------------------------------------------------------------------- */

@Composable
fun WifiListPage(
    bottomBaronClickedActions: List<() -> Unit>,
    moveToFirstMainPage: () -> Unit,
    pinnedWifiName: String,
    setPinnedWifiName: (String) -> Unit,
    navToHelpPage: () -> Unit
) {
    /* ---------- 권한 요청 ---------- */
    val context = LocalContext.current
    val locationGranted = remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        locationGranted.value = result.values.all { it }
    }

    LaunchedEffect(Unit) {
        val has = ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (has) locationGranted.value = true
        else permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    /* ---------- 스캔 결과 상태 ---------- */
    val wifiDistances by produceState(
        initialValue = emptyList<WifiDisplay>(),
        key1 = locationGranted.value
    ) {
        if (!locationGranted.value) {
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
                val results = wifiManager.scanResults
                value = results.toDisplayList(ukfMap)
            }
        }
        context.registerReceiver(receiver,
            IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))

        /* 5 초 주기로 스캔 반복 */
        while (true) {
            try { wifiManager.startScan() } catch (_: Exception) { /* ignore */ }
            delay(5_000L)
        }

        /* onDispose – produceState 자동 해제 */
        @Suppress("UNREACHABLE_CODE")
        context.unregisterReceiver(receiver)
    }

    /* 분류 */
    // @todo: suspicious를 BE PCAP features에 던져서 가져오는 걸로 변경할 것. 지금은 가장 가까운 2개를 임의로 suspicious에 넣고 있음.
    val suspicious = wifiDistances.take(2)
    val others     = wifiDistances.drop(2)

    /* ---------- UI ---------- */
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainPageTopBar(
                pinnedWifiName = pinnedWifiName,
                navToHelpPage   = navToHelpPage
            )
        },
        bottomBar = {
            BottomMenu(
                bottomBaronClickedActions = bottomBaronClickedActions,
                currentScreen = "MainPage"
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            /* --------------  (1) 현재 의심스러운 기기 -------------- */
            Column(
                modifier = Modifier
                    .weight(0.75f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "현재 의심스러운 기기가 근처에 ${suspicious.size}대 있습니다.",
                    color = TextColorGray,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )

                WifiBox(list = suspicious,
                    showFindButton = true,
                    setPinnedWifiName = setPinnedWifiName)

                Spacer(modifier = Modifier.padding(vertical = 16.dp))

                /* --------------  (2) 그 외 멀리 있는 기기 -------------- */
                Text(
                    text = "그 외 멀리 있는 의심스러운 기기들",
                    color = TextColorGray,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )

                WifiBox(list = others,
                    showFindButton = false,
                    setPinnedWifiName = setPinnedWifiName)
            }

            /* --------------  (3) 하단 버튼 -------------- */
//            Column(
//                modifier = Modifier.weight(0.25f),
//                verticalArrangement = Arrangement.Bottom
//            ) {
//                CustomButton(text = "다시 스캔하기") {
//                    /* 즉시 재스캔 – produceState 내부 loop가 5 s마다 실행되므로
//                       여기서는 권한 확인 후 startScan()만 트리거 */
//                    val wm =
//                        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
//                    if (locationGranted.value) try { wm.startScan() } catch (_: Exception) { }
//                    moveToFirstMainPage()
//                }
//            }
        }
    }
}

/* -------------------------------------------------------------------------- */
/* ---------------------------  UI 헬퍼 컴포넌트  --------------------------- */
/* -------------------------------------------------------------------------- */

@Composable
private fun WifiBox(
    list: List<WifiDisplay>,
    showFindButton: Boolean,
    setPinnedWifiName: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(48.dp))
            .background(Color(0xFFE7EDE4))
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(vertical = 8.dp)
        ) {
            if (list.isEmpty()) {
                Text(
                    text = "표시할 Wi-Fi가 없습니다.",
                    color = TextColorGray,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                list.forEach { info ->
                    WifiComponent(
                        onSearchClicked       = { setPinnedWifiName(info.ssid) },
                        name                  = info.ssid,
                        distance              = info.distanceString,
                        showFindButtonOrNot   = showFindButton
                    )
                }
            }
        }
    }
}

/* -------------------------------------------------------------------------- */
/* ----------------------------  데이터 & 계산  ----------------------------- */
/* -------------------------------------------------------------------------- */

private data class WifiDisplay(
    val ssid: String,
    val distance: Double
) {
    val distanceString: String =
        String.format(Locale.US, "%.2f m", distance)
}

private fun List<ScanResult>.toDisplayList(
    ukfMap: MutableMap<String, WifiUkf>
): List<WifiDisplay> =
    this
        .filter { it.level >= MIN_RSSI }                       // 신호 필터링
        .map { res ->
            val raw  = rssiToDistance(res.level)
            val ukf  = ukfMap.getOrPut(res.BSSID) { WifiUkf(initial = raw) }
            val dist = ukf.update(raw)
            WifiDisplay(res.SSID.ifBlank { res.BSSID }, dist)
        }
        .sortedBy { it.distance }

/* ---------- RSSI → 거리 ---------- */
private fun rssiToDistance(rssi: Int, walls: Int = 1): Double {
    val totalLoss = (RSSI_AT_1M - rssi) - (walls * WALL_LOSS_DB)
    return 10.0.pow(totalLoss / (10 * PATH_LOSS_EXPONENT))
}

/* -------------------------------------------------------------------------- */
/* ---------------------------  UKF 1-차원 구현  ---------------------------- */
/* -------------------------------------------------------------------------- */

private class WifiUkf(
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
        val sqrtTerm = sqrt(c * PPred)
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
            S   += wc[i] * dz * dz
            Cxz += wc[i] * dx * dz
        }

        val K = Cxz / S
        x = xPred + K * (z - zPred)
        P = PPred - K * S * K

        return x
    }
}

/* ---------- 상수 ---------- */
private const val RSSI_AT_1M       = -40      // [dBm] 1 m 기준
private const val PATH_LOSS_EXPONENT = 3.0
private const val WALL_LOSS_DB     = 1
private const val MIN_RSSI         = -80
