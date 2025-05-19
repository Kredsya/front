package com.example.frontcapstone2025.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.frontcapstone2025.components.layout.BottomMenu
import com.example.frontcapstone2025.components.layout.MainPageTopBar
import com.example.frontcapstone2025.ui.theme.MainBackgroundColor
import com.example.frontcapstone2025.ui.theme.TextColorGray

@Composable
fun MainPage() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackgroundColor),
        topBar = { MainPageTopBar() },
        bottomBar = { BottomMenu() },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            Text(text = "Mainpage", color = TextColorGray)
        }

    }
}