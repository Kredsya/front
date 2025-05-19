package com.example.frontcapstone2025.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.frontcapstone2025.components.items.BottomBarIcon
import com.example.frontcapstone2025.ui.theme.BottomBarBackground
import com.example.frontcapstone2025.ui.theme.BottomBarClickedIconColor
import com.example.frontcapstone2025.ui.theme.BottomBarIconColor

@Composable
@Preview(showBackground = true)
fun BottomMenu() {
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
                    icon = Icons.Default.Warning, //Todo 이거 모양 찾아라
                    onClicked = { /*TODO*/ },
                    color = BottomBarIconColor
                )
                BottomBarIcon(
                    name = "Main",
                    icon = Icons.Default.Home,
                    onClicked = { /*TODO*/ },
                    color = BottomBarClickedIconColor
                )
                BottomBarIcon(
                    name = "Setting",
                    icon = Icons.Default.Settings,
                    onClicked = { /*TODO*/ },
                    color = BottomBarIconColor
                )
            }
        },
        containerColor = BottomBarBackground,
    )

}