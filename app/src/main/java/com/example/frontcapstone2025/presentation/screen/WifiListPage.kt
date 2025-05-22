package com.example.frontcapstone2025.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.frontcapstone2025.components.buttons.CustomButton
import com.example.frontcapstone2025.components.items.WifiComponent
import com.example.frontcapstone2025.components.layout.BottomMenu
import com.example.frontcapstone2025.components.layout.MainPageTopBar
import com.example.frontcapstone2025.ui.theme.TextColorGray

@Composable
fun WifiListPage(
    bottomBaronClickedActions: List<() -> Unit>,
    moveToFirstMainPage: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { MainPageTopBar() },
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
            Column(
                modifier = Modifier
                    .weight(0.75f)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "현재 의심스러운 기기가 근처에 n대 있습니다.",
                    color = TextColorGray,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(48.dp)) // 둥근 사각형 테두리
                        .background(Color(0xFFE7EDE4))   // 연한 배경색
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        WifiComponent()
                        WifiComponent()
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = 16.dp))

                Text(
                    text = "그 외 멀리 있는 의심스러운 기기들",
                    color = TextColorGray,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(48.dp)) // 둥근 사각형 테두리
                        .background(Color(0xFFE7EDE4))   // 연한 배경색
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .padding(vertical = 8.dp)
                    ) {
                        WifiComponent()
                        WifiComponent()
                        WifiComponent()
                        WifiComponent()
                    }
                }
            }
            Column(
                modifier = Modifier.weight(0.25f),
                verticalArrangement = Arrangement.Bottom
            ) {
                CustomButton(text = "다시 스캔하기", onClicked = moveToFirstMainPage)
            }
        }
    }
}