package com.example.frontcapstone2025.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontcapstone2025.components.layout.BottomMenu
import com.example.frontcapstone2025.components.layout.MainPageTopBar
import com.example.frontcapstone2025.utility.WifiConfig
import com.example.frontcapstone2025.ui.theme.BottomBarBackground
import com.example.frontcapstone2025.ui.theme.DivideLineColor
import com.example.frontcapstone2025.ui.theme.TextColorGray
import com.example.frontcapstone2025.viemodel.MainViewModel

@Composable
fun SettingPage(
    bottomBaronClickedActions: List<() -> Unit>,
    navToHelpPage: () -> Unit,
    mainViewModel: MainViewModel
) {
    var locationPermission by rememberSaveable { mutableStateOf(true) }
    var storagePermission by rememberSaveable { mutableStateOf(true) }

    var rssiAt1m by rememberSaveable { mutableStateOf(WifiConfig.rssiAt1m.toFloat()) }
    var pathLossExponent by rememberSaveable { mutableStateOf(WifiConfig.pathLossExponent.toFloat()) }
    var wallLossDb by rememberSaveable { mutableStateOf(WifiConfig.wallLossDb.toFloat()) }
    var minRssi by rememberSaveable { mutableStateOf(WifiConfig.minRssi.toFloat()) }

    val chosenWifi by mainViewModel.chosenWifi.collectAsState()
    Scaffold(
        topBar = {
            MainPageTopBar(
                pinnedWifiName = chosenWifi,
                navToHelpPage = navToHelpPage
            )
        },
        bottomBar = {
            BottomMenu(
                bottomBaronClickedActions = bottomBaronClickedActions,
                currentScreen = "SettingPage"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            ) {
                // 제목
                Text(
                    text = "권한 관리",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.ExtraBold),
                    color = TextColorGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    thickness = 3.dp,
                    color = DivideLineColor
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 설명 텍스트
                Text(
                    buildAnnotatedString {
                        append("아래 권한을 전부 허용하지 않으면 ")
                        withStyle(
                            style = SpanStyle(
                                color = Color.Red,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("앱의 기능을 사용할 수 없습니다.")
                        }
                    },
                    color = TextColorGray,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 위치 권한 스위치
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Switch(
                            checked = locationPermission,
                            onCheckedChange = { locationPermission = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = BottomBarBackground,
                                checkedTrackColor = DivideLineColor
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "위치 권한",
                            color = TextColorGray,
                            modifier = Modifier.height(28.dp),
                            fontSize = 16.sp
                        )
                    }


                    // 저장 공간 권한 스위치
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Switch(
                            checked = storagePermission,
                            onCheckedChange = { storagePermission = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = BottomBarBackground,
                                checkedTrackColor = DivideLineColor
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "저장 공간 권한",
                            color = TextColorGray,
                            modifier = Modifier.height(28.dp),
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Wifi 설정 슬라이더
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("RSSI at 1m: ${'$'}{rssiAt1m.toInt()} dBm", color = TextColorGray)
                        Slider(
                            value = rssiAt1m,
                            onValueChange = {
                                rssiAt1m = it
                                WifiConfig.rssiAt1m = it.toInt()
                            },
                            valueRange = -90f..-20f
                        )

                        Text("Path Loss Exponent: ${'$'}{String.format(\"%.2f\", pathLossExponent)}", color = TextColorGray)
                        Slider(
                            value = pathLossExponent,
                            onValueChange = {
                                pathLossExponent = it
                                WifiConfig.pathLossExponent = it.toDouble()
                            },
                            valueRange = 1f..5f
                        )

                        Text("Wall Loss dB: ${'$'}{wallLossDb.toInt()}", color = TextColorGray)
                        Slider(
                            value = wallLossDb,
                            onValueChange = {
                                wallLossDb = it
                                WifiConfig.wallLossDb = it.toInt()
                            },
                            valueRange = 0f..10f
                        )

                        Text("Min RSSI: ${'$'}{minRssi.toInt()} dBm", color = TextColorGray)
                        Slider(
                            value = minRssi,
                            onValueChange = {
                                minRssi = it
                                WifiConfig.minRssi = it.toInt()
                            },
                            valueRange = -100f..-30f
                        )
                    }
                }
            }
        }
    }
}
