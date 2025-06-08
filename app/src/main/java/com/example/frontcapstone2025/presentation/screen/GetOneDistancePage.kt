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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.example.frontcapstone2025.components.buttons.CustomButton
import com.example.frontcapstone2025.components.items.LoadingComponent
import com.example.frontcapstone2025.components.layout.TopBarWithBack
import com.example.frontcapstone2025.ui.theme.TextColorGray
import com.example.frontcapstone2025.viemodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GetOneDistancePage(
    id: Int,
    navigationBack: () -> Unit,
    @DrawableRes imageResId: Int,
    navToHelpPage: () -> Unit,
    mainViewModel: MainViewModel
) {
    val chosenWifi by mainViewModel.chosenWifi.collectAsState()
    val distance by mainViewModel.getDistanceById(id).collectAsState()
//    val loadingTime by mainViewModel.initialLoadingTime.collectAsState()
    val context = LocalContext.current

    var isLoading by rememberSaveable { mutableStateOf(false) }

    if (isLoading) {
        LoadingComponent(text = "수상한 기기와의 거리를 측정하고 있어요.") // 로딩 중일 때 표시
    } else {

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopBarWithBack(
                    navigationBack = navigationBack,
                    pinnedWifiName = chosenWifi,
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
                    text = if (distance != -1.0) "거리 : $distance" else "거리 : ",
                    color = TextColorGray,
                )
                CustomButton(
                    text = "측정 시작하기",
                    onClicked = {
                        mainViewModel.setDistanceById(id, -1.0)
                        isLoading = true
                        mainViewModel.viewModelScope.launch {
//                            mainViewModel.scanAndSaveDistance(id, chosenWifi, context)
//                            delay(loadingTime) // 🕒 로딩 지속 시간
                            mainViewModel.scanDistanceRepeatedly(id, chosenWifi, context)
                            isLoading = false
                        }
                    }
                )

            }
        }
    }
}