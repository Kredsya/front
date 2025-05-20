package com.example.frontcapstone2025.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontcapstone2025.components.items.BottomBarIcon
import com.example.frontcapstone2025.ui.theme.BottomBarBackground
import com.example.frontcapstone2025.ui.theme.BottomBarClickedIconColor
import com.example.frontcapstone2025.ui.theme.BottomBarIconColor

//Todo 클릭하면 전체가 클릭되는 모션 없애야함
@Composable
fun BottomMenu(bottomBaronClickedActions: List<() -> Unit>, currentScreen: String) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(0, 0, 0, 0), // 시스템바 피하는 패딩 없애기 (빌드해보고 조절 필요)
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                BottomBarIcon(
                    name = "Radar",
                    icon = Icons.Default.WifiTethering, //Todo 이거 모양 찾아라
                    onClicked = bottomBaronClickedActions[0],
                    color = if (currentScreen == "SearchWifiPage") BottomBarClickedIconColor else BottomBarIconColor
                )
                BottomBarIcon(
                    name = "Main",
                    icon = Icons.Default.Home,
                    onClicked = bottomBaronClickedActions[1],
                    color = if (currentScreen == "MainPage") BottomBarClickedIconColor else BottomBarIconColor
                )
                BottomBarIcon(
                    name = "Setting",
                    icon = Icons.Default.Settings,
                    onClicked = bottomBaronClickedActions[2],
                    color = if (currentScreen == "SettingPage") BottomBarClickedIconColor else BottomBarIconColor
                )
            }
        },
        containerColor = BottomBarBackground,
    )

}