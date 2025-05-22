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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.frontcapstone2025.ui.theme.BottomBarBackground
import com.example.frontcapstone2025.ui.theme.DivideLineColor
import com.example.frontcapstone2025.ui.theme.TextColorGray

@Composable
fun SettingPage(
    bottomBaronClickedActions: List<() -> Unit>,
    pinnedWifiName: String,
    navToHelpPage: () -> Unit
) {
    var locationPermission by rememberSaveable { mutableStateOf(true) }
    var storagePermission by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        topBar = {
            MainPageTopBar(
                pinnedWifiName = pinnedWifiName,
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
                }
            }
        }
    }
}
