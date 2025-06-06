package com.example.frontcapstone2025.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.frontcapstone2025.components.buttons.CustomButton
import com.example.frontcapstone2025.components.layout.BottomMenu
import com.example.frontcapstone2025.components.layout.MainPageTopBar
import com.example.frontcapstone2025.viemodel.MainViewModel

@Composable
fun SearchWifiPage(
    bottomBaronClickedActions: List<() -> Unit>,
    moveToGetArmLengthPage: () -> Unit,
    navToHelpPage: () -> Unit,
    mainViewModel: MainViewModel
) {
    val chosenWifi by mainViewModel.chosenWifi.collectAsState()
    val showDialog by mainViewModel.showDialog.collectAsState()
    val wifiPosition by mainViewModel.wifiPosition.collectAsState()

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
                currentScreen = "SearchWifiPage"
            )
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomButton(
                text = "카메라 위치 찾기",
                onClicked = moveToGetArmLengthPage,
                enabled = chosenWifi.isNotBlank()
            )
        }

        if (showDialog) {
            NotifyCameraLocationDialog(
                x = wifiPosition.x,
                y = wifiPosition.y,
                z = wifiPosition.z,
                meterChanged = mainViewModel.isMeterChanged(),
                onCloseClicked = { mainViewModel.setShowDialog(false) },
            )
        }
    }

}