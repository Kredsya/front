package com.example.frontcapstone2025.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontcapstone2025.presentation.screen.LoadingPage
import com.example.frontcapstone2025.presentation.screen.MainPage
import com.example.frontcapstone2025.presentation.screen.SearchWifiPage
import com.example.frontcapstone2025.presentation.screen.SettingPage

fun navivationWithClear(navController: NavController, route: String) {
    navController.popBackStack()
    navController.navigate(route)
}

@Composable
fun Navigator() {
    val navController = rememberNavController()

    val navigationBack: () -> Unit = { navController.navigateUp() }

    val bottomBaronClickedActions = listOf(
        { navivationWithClear(navController = navController, route = "SearchWifiPage") },
        { navivationWithClear(navController = navController, route = "MainPage") },
        { navivationWithClear(navController = navController, route = "SettingPage") },
    )

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "MainPage",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = "MainPage") {
                MainPage(
                    bottomBaronClickedActions = bottomBaronClickedActions,
                    moveToLoadingPage = { navController.navigate("LoadingPage") }
                )
            }
            composable(route = "SettingPage") {
                SettingPage(bottomBaronClickedActions = bottomBaronClickedActions)
            }
            composable(route = "SearchWifiPage") {
                SearchWifiPage(bottomBaronClickedActions = bottomBaronClickedActions)
            }


            composable(route = "LoadingPage") {
                LoadingPage(text = "주변 네트워크를 검색하고 있어요.\n대략 30~40초 정도 걸려요.")
            }
        }
    }
}
