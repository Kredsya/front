package com.example.frontcapstone2025.presentation.screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.frontcapstone2025.R
import com.example.frontcapstone2025.components.buttons.CustomButton
import com.example.frontcapstone2025.components.layout.TopBarWithBack
import com.example.frontcapstone2025.ui.theme.TextColorGray
import com.example.frontcapstone2025.viemodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun GetAllDistancePage(
    navigationBack: () -> Unit,
    navToOneDistancePage: List<() -> Unit>,
    navToHelpPage: () -> Unit,
    mainViewModel: MainViewModel,
    navToHome: () -> Unit
) {
    val chosenWifi by mainViewModel.chosenWifi.collectAsState()
    val upDistance by mainViewModel.upDistance.collectAsState()
    val downDistance by mainViewModel.downDistance.collectAsState()
    val leftDistance by mainViewModel.leftDistance.collectAsState()
    val frontDistance by mainViewModel.frontDistance.collectAsState()
    val armLength by mainViewModel.armLength.collectAsState()

    val upImage = painterResource(R.drawable.up)
    val downImage = painterResource(R.drawable.down)
    val leftImage = painterResource(R.drawable.left)
    val frontImage = painterResource(R.drawable.front)

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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFB4C5AB))
                    .padding(16.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Column {
                    Text(
                        text = "※ 주의",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        color = TextColorGray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = buildAnnotatedString {
                            append("1. ")
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("왼팔이 자유롭게")
                            }
                            append(" 움직일 수 있는 위치로 이동 후, 결과를 얻을 때까지 ")
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("움직이지 마세요.")
                            }
                        },
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        color = TextColorGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = buildAnnotatedString {
                            append("2. ")
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append("핸드폰을 왼손")
                            }
                            append("에 쥐고, 사진의 동작을 따라해주세요.")
                        },
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        color = TextColorGray
                    )
                }

            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Image(
                        painter = upImage,
                        contentDescription = null,
                        modifier = Modifier
                            .height(196.dp)
                            .clickable { navToOneDistancePage[0]() }
                    )
                    Text(
                        text = if (upDistance != -1.0) "측정: %.4f m".format(upDistance) else "측정: X",
                        modifier = Modifier,
                        color = TextColorGray
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Image(
                        painter = downImage,
                        contentDescription = null,
                        modifier = Modifier
                            .height(196.dp)
                            .clickable { navToOneDistancePage[1]() }
                    )
                    Text(
                        text = if (downDistance != -1.0) "측정: %.4f m".format(downDistance) else "측정: X",
                        modifier = Modifier,
                        color = TextColorGray

                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Image(
                        painter = leftImage,
                        contentDescription = null,
                        modifier = Modifier
                            .height(196.dp)
                            .clickable { navToOneDistancePage[2]() }
                    )
                    Text(
                        text = if (leftDistance != -1.0) "측정: %.4f m".format(leftDistance) else "측정: X",
                        modifier = Modifier,
                        color = TextColorGray
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.5f)
                ) {
                    Image(
                        painter = frontImage,
                        contentDescription = null,
                        modifier = Modifier
                            .height(196.dp)
                            .clickable { navToOneDistancePage[3]() }
                    )
                    Text(
                        text = if (frontDistance != -1.0) "측정: %.4f m".format(frontDistance) else "측정: X",
                        modifier = Modifier,
                        color = TextColorGray

                    )
                }
            }

            CustomButton(
                text = "거리 측정 완료",
                onClicked = {
                    mainViewModel.viewModelScope.launch {
                        mainViewModel.getWifiPosition()
                        mainViewModel.setShowDialog(true)
                        navToHome()
                    }
                },
                enabled = (
                        upDistance != -1.0 &&
                                downDistance != -1.0 &&
                                leftDistance != -1.0 &&
                                frontDistance != -1.0 &&
                                armLength != -1.0
                        ),
            )
        }


    }
}