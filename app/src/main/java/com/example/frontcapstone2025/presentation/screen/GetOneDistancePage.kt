package com.example.frontcapstone2025.presentation.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.frontcapstone2025.components.buttons.CustomButton
import com.example.frontcapstone2025.components.layout.TopBarWithBack
import com.example.frontcapstone2025.ui.theme.TextColorGray

@Composable
fun GetOneDistancePage(
    navigationBack: () -> Unit,
    distance: String,
    @DrawableRes imageResId: Int,
    pinnedWifiName: String,
    navToHelpPage: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBarWithBack(
                navigationBack = navigationBack,
                pinnedWifiName = pinnedWifiName,
                navToHelpPage = navToHelpPage
            )
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = null,
                modifier = Modifier
                    .height(496.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )
            Text(
                text = "거리 : $distance",
                color = TextColorGray,
            )
            CustomButton(
                text = "측정 시작하기",
                onClicked = {}
            )
        }
    }
}