package com.example.frontcapstone2025.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.frontcapstone2025.components.buttons.CircularSearchButton
import com.example.frontcapstone2025.components.buttons.CustomButton
import com.example.frontcapstone2025.components.layout.BottomMenu
import com.example.frontcapstone2025.components.layout.MainPageTopBar
import com.example.frontcapstone2025.viemodel.MainViewModel

@Composable
fun MainPage(
    bottomBaronClickedActions: List<() -> Unit>,
    moveToSearchWifiListPage: () -> Unit,
    navToHelpPage: () -> Unit,
    mainViewModel: MainViewModel
) {
    val chosenWifi by mainViewModel.chosenWifi.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            MainPageTopBar(
                pinnedWifiName = chosenWifi,
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
                CircularSearchButton(onClicked = moveToSearchWifiListPage)
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                val context = LocalContext.current
                CustomButton(
                    text = "스캔 시작",
                    onClicked = {
                        mainViewModel.startCaptureAndAnalyze(context)
                        moveToSearchWifiListPage()
                    }
                )
            }
        }
    }
}