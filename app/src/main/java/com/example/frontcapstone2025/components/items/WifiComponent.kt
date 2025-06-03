package com.example.frontcapstone2025.components.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontcapstone2025.ui.theme.TextColorGray

@Composable
fun WifiComponent(
    onSearchClicked: () -> Unit,
    name: String,
    distance: String,
    showFindButtonOrNot: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Wi-Fi 아이콘
        Icon(
            imageVector = Icons.Default.Wifi,
            contentDescription = "Wi-Fi Icon",
            modifier = Modifier.size(48.dp),
            tint = Color.Black
        )

        Spacer(modifier = Modifier.width(8.dp))

        // SSID 텍스트
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = TextColorGray
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 거리 텍스트만 하단 정렬
        Box(
            modifier = Modifier
                .weight(1f)
                .height(64.dp),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Text(
                text = distance,
                fontSize = 16.sp,
                color = TextColorGray,
                modifier = Modifier.padding(end = 8.dp, bottom = 10.dp)
            )
        }

        // '찾기' 버튼
        Button(
            onClick = {
                if (showFindButtonOrNot) onSearchClicked()
            }, // 클릭 동작도 연결
            shape = RoundedCornerShape(4.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 3.dp,
                disabledElevation = 0.dp
            ),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (showFindButtonOrNot) Color.White else Color.Gray, // ✅ 색상 조건 분기
                contentColor = TextColorGray
            ),
            modifier = Modifier
                .width(56.dp)
                .height(32.dp),
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),

            ) {
            Text(
                text = "찾기",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
            )
        }
    }


}

