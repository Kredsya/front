package com.example.frontcapstone2025

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontcapstone2025.presentation.navigation.Navigator
import com.example.frontcapstone2025.ui.theme._2025FrontCapstoneTheme
import com.example.frontcapstone2025.viemodel.MainViewModel

class MainActivity : ComponentActivity() {
    companion object {
        const val CAPTURE_STATUS_ACTION = "com.example.frontcapstone2025.CAPTURE_STATUS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _2025FrontCapstoneTheme {
                val mainViewModel = viewModel<MainViewModel>()
                Navigator(mainViewModel = mainViewModel)
            }
        }
    }
}
