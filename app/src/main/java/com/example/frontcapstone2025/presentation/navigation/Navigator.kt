package com.example.frontcapstone2025.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontcapstone2025.presentation.screen.MainPage

@Composable
fun Navigator() {
    val navController = rememberNavController()

    val navigationBack: () -> Unit = { navController.navigateUp() }

//    navController.addOnDestinationChangedListener { _, destination, _ ->
//        when (destination.route) {
//            "SearchPage" -> {
//                searchText = ""
//                mainViewModel.clearSearchedBookList()
//            }
//
//        }
//    }

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "MainPage",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = "MainPage") {
                MainPage()
            }
        }
    }
}
