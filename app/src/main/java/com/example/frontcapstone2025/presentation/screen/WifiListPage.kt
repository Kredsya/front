package com.example.frontcapstone2025.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
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
import com.example.frontcapstone2025.components.buttons.CustomButton
import com.example.frontcapstone2025.components.items.WifiComponent
import com.example.frontcapstone2025.components.layout.BottomMenu
import com.example.frontcapstone2025.components.layout.MainPageTopBar
import com.example.frontcapstone2025.ui.theme.TextColorGray
import com.example.frontcapstone2025.utility.WifiDisplay
import com.example.frontcapstone2025.utility.rememberWifiDistances

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
    val wifiDistances by rememberWifiDistances(locationGranted.value)

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
