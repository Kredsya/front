package com.example.frontcapstone2025.components.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun BottomBarIcon(
    modifier: Modifier = Modifier,
    name: String,
    icon: ImageVector,
    color: Color,
    onClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClicked)
            .fillMaxHeight()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            modifier = Modifier.size(60.dp),
            onClick = { onClicked() }
        ) {
            androidx.compose.material3.Icon(
                modifier = modifier.size(50.dp),
                imageVector = icon,
                contentDescription = name,
                tint = color,
            )
        }
    }
}