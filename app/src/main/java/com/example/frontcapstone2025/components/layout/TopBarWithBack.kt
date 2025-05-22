package com.example.frontcapstone2025.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontcapstone2025.R
import com.example.frontcapstone2025.ui.theme.BottomBarIconColor
import com.example.frontcapstone2025.ui.theme.TextColorGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBack(
    navigationBack: () -> Unit,
    pinnedWifiName: String,
    navToHelpPage: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = "",
                textAlign = TextAlign.Center,
                color = TextColorGray,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = navigationBack) {
                    androidx.compose.material3.Icon(
                        tint = BottomBarIconColor,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(34.dp)
                    )
                }

                IconButton(
                    modifier = Modifier
                        .size(48.dp)
                        .fillMaxWidth(0.1f),
                    onClick = { navToHelpPage() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.keep_icon), // ✅ 변경된 부분
                        contentDescription = "keep",
                        modifier = Modifier.size(34.dp),
                        tint = BottomBarIconColor
                    )
                }
                Text(
                    text = pinnedWifiName,
                    color = TextColorGray,
                    fontSize = 24.sp,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                IconButton(
                    modifier = Modifier
                        .size(48.dp)
                        .fillMaxWidth(0.1f),
                    onClick = { }
                ) {
                    androidx.compose.material3.Icon(
                        modifier = Modifier.size(34.dp),
                        imageVector = Icons.AutoMirrored.Filled.Help, //  Todo Keep 이 없음. 수정 필요
                        contentDescription = "help",
                        tint = BottomBarIconColor,
                    )
                }
            }
        }
    )
}
