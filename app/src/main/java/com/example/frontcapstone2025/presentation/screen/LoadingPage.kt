package com.example.frontcapstone2025.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.frontcapstone2025.ui.theme.BottomBarClickedIconColor
import com.example.frontcapstone2025.ui.theme.LoadingTrackColor
import com.example.frontcapstone2025.ui.theme.TextColorGray

@Composable
fun LoadingPage(
    text: String,
    navController: NavController
) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000) // 2초간 대기 (비동기 처리 시 네트워크 작업으로 대체 가능)
        navController.navigate("WifiListPage") {
            popUpTo("loading_screen") { inclusive = true } // 뒤로가기 안 되게 처리
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp),
                strokeWidth = 10.dp,
                trackColor = LoadingTrackColor,
                color = BottomBarClickedIconColor
            )
            Text(
                text = text,
                color = TextColorGray,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}
