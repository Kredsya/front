package com.example.frontcapstone2025.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontcapstone2025.ui.theme.TextColorGray

@Composable
fun CustomButton(
    text: String,
    onClicked: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClicked,
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = TextColorGray
        ),
        modifier = Modifier
            .padding(32.dp)
            .fillMaxWidth()
            .height(80.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp))
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
    }
}
