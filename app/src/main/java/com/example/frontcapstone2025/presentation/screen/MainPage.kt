package com.example.frontcapstone2025.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.frontcapstone2025.components.buttons.CircularSearchButton
import com.example.frontcapstone2025.components.buttons.CustomButton
import com.example.frontcapstone2025.components.layout.BottomMenu
import com.example.frontcapstone2025.components.layout.MainPageTopBar
import com.example.frontcapstone2025.viemodel.MainViewModel

@Composable
fun MainPage(
    bottomBaronClickedActions: List<() -> Unit>,
    moveToLoadingPage: () -> Unit,
    pinnedWifiName: String = "",
    navToHelpPage: () -> Unit,
    mainViewModel: MainViewModel
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            MainPageTopBar(
                pinnedWifiName = pinnedWifiName,
                navToHelpPage = navToHelpPage
            )
        },
        bottomBar = {
            BottomMenu(
                bottomBaronClickedActions = bottomBaronClickedActions,
                currentScreen = "MainPage"
            )
        },

        ) { innerPadding ->

        LaunchedEffect(Unit) {
            mainViewModel.getWifiPosition()
        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                CircularSearchButton(onClicked = moveToLoadingPage)
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                CustomButton(text = "스캔 시작", onClicked = moveToLoadingPage)
            }
        }
    }
}