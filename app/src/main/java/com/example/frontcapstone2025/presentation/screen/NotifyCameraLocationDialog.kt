package com.example.frontcapstone2025.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontcapstone2025.ui.theme.BottomBarIconColor
import com.example.frontcapstone2025.ui.theme.TextColorGray

@Composable
fun NotifyCameraLocationDialog(
    x: Double,
    y: Double,
    z: Double,
    onCloseClicked: () -> Unit,
    meterChanged: Boolean = false,
) {

    // 각도 계산 (라디안 → 도)
    val angleRadians = kotlin.math.atan2(y, x)
    val angleDegrees = Math.toDegrees(angleRadians)


    // 다이얼로그 배경 (투명한 검정)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        // 팝업 박스
        Box(
            modifier = Modifier
//                .align(LineHeightStyle.Alignment.Center)
                .background(Color(0xFFDCE5D5), shape = RoundedCornerShape(8.dp))  // 연한 녹색
                .padding(24.dp)
                .clickable(enabled = false) {}  // 내부 클릭 이벤트 차단
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "카메라 위치",
                        fontWeight = FontWeight.Bold,
                        color = TextColorGray,
                        fontSize = 24.sp
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "닫기",
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { onCloseClicked() },
                        tint = BottomBarIconColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 화살표 (회전)
                Icon(
                    imageVector = Icons.Default.ArrowUpward,
                    contentDescription = "방향 화살표",
                    modifier = Modifier
                        .size(100.dp)
                        .rotate(angleDegrees.toFloat()),  // Compose는 rotate로 회전
                    tint = BottomBarIconColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (meterChanged) {
                        Text(text = "측정자의 왼쪽 어깨 기준", color = TextColorGray)
                        Text(text = "x : %.2f m".format(x), color = TextColorGray)
                        Text(text = "y : %.2f m".format(y), color = TextColorGray)
                        Text(text = "z : %.2f m".format(z), color = TextColorGray)
                    } else {
                        Text(text = "측정자의 왼쪽 어깨 기준", color = TextColorGray)
                        Text(text = "x : %.2f cm".format(x), color = TextColorGray)
                        Text(text = "y : %.2f cm".format(y), color = TextColorGray)
                        Text(text = "z : %.2f cm".format(z), color = TextColorGray)
                    }

                }
            }
        }
    }
}
