package com.example.frontcapstone2025.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.frontcapstone2025.components.layout.TopBarWithBack
import com.example.frontcapstone2025.ui.theme.DivideLineColor
import com.example.frontcapstone2025.ui.theme.TextColorGray

@Composable
fun GetArmLengthPage(
    navigationBack: () -> Unit
) {
    var armLength by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { TopBarWithBack(navigationBack = navigationBack) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "팔 길이를 측정해서 입력하세요.\n만약 측정이 불가능할 경우 다음 평균 수치를 참고하세요.\n" +
                        "성인 평균 : 여성 60~65cm / 남성 65~70cm",
                modifier = Modifier.padding(bottom = 8.dp),
                color = TextColorGray
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = armLength,
                    onValueChange = { ar ->
                        if (ar.matches(Regex("^\\d{0,3}(\\.\\d{0,2})?$"))) {
                            armLength = ar
                        }
                    },
                    placeholder = { Text("팔 길이를 입력하세요. (예: 64.2)") },
                    singleLine = true,
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone = {
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = TextColorGray,
                        unfocusedTextColor = TextColorGray,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = DivideLineColor,
                        disabledPlaceholderColor = TextColorGray,
                        focusedPlaceholderColor = TextColorGray,
                        unfocusedPlaceholderColor = TextColorGray,
                        disabledIndicatorColor = DivideLineColor,
                        unfocusedIndicatorColor = DivideLineColor,
                        cursorColor = DivideLineColor
                    )

                )
                Text(
                    text = "cm",
                    modifier = Modifier.padding(start = 8.dp),
                    color = TextColorGray
                )
            }

        }
    }
}
