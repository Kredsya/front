package com.example.frontcapstone2025.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.frontcapstone2025.ui.theme.BottomBarIconColor

@Composable

fun CircularSearchButton(
    onClicked: () -> Unit
) {
    // 바깥 고리용 박스
    Box(
        modifier = Modifier
            .size(100.dp)
            .background(color = Color.Transparent, shape = CircleShape)
            .border(
                width = 4.dp,
                color = Color.White, // 바깥 고리처럼 보이게
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        // 내부 버튼 + 그림자
        Box(
            modifier = Modifier
                .size(80.dp)
                .shadow(8.dp, CircleShape, clip = false)
                .background(color = Color.White, shape = CircleShape)
                .clickable { onClicked() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = BottomBarIconColor,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
