package com.example.frontcapstone2025.presentation.navigation

import HelpPage
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.frontcapstone2025.R
import com.example.frontcapstone2025.presentation.screen.GetAllDistancePage
import com.example.frontcapstone2025.presentation.screen.GetArmLengthPage
import com.example.frontcapstone2025.presentation.screen.GetOneDistancePage
import com.example.frontcapstone2025.presentation.screen.LoadingPage
import com.example.frontcapstone2025.presentation.screen.MainPage
import com.example.frontcapstone2025.presentation.screen.SearchWifiPage
import com.example.frontcapstone2025.presentation.screen.SettingPage
import com.example.frontcapstone2025.presentation.screen.WifiListPage

fun navivationWithClear(navController: NavController, route: String) {
    navController.popBackStack()
    navController.navigate(route)
}

@Composable
fun Navigator() {
    val navController = rememberNavController()

    val navigationBack: () -> Unit = { navController.navigateUp() }

    var centerButtonTarget by rememberSaveable { mutableStateOf("MainPage") }
    val bottomBaronClickedActions = listOf(
        { navivationWithClear(navController = navController, route = "SearchWifiPage") },
        {
            navivationWithClear(navController, centerButtonTarget) // 상태 기반으로 이동
        },
        { navivationWithClear(navController = navController, route = "SettingPage") },
    )

    var pinnedWifiName by rememberSaveable { mutableStateOf("") }

    var showDialog by rememberSaveable { mutableStateOf(false) }


    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "MainPage",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = "MainPage") {
                MainPage(
                    bottomBaronClickedActions = bottomBaronClickedActions,
                    moveToLoadingPage = {
                        navController.navigate("LoadingPage")
                        centerButtonTarget = "WifiListPage"
                    },
                    pinnedWifiName = pinnedWifiName,
                    navToHelpPage = { navController.navigate("HelpPage") }

                )
            }
            composable(route = "SettingPage") {
                SettingPage(
                    bottomBaronClickedActions = bottomBaronClickedActions,
                    pinnedWifiName = pinnedWifiName,
                    navToHelpPage = { navController.navigate("HelpPage") }

                )
            }
            composable(route = "SearchWifiPage") {
                SearchWifiPage(
                    bottomBaronClickedActions = bottomBaronClickedActions,
                    moveToGetArmLengthPage = { navController.navigate("GetArmLengthPage") },
                    pinnedWifiName = pinnedWifiName,
                    navToHelpPage = { navController.navigate("HelpPage") },
                    showDialog = showDialog,
                    setShowDialog = { showDialog = it }

                )
            }

            composable(route = "WifiListPage") {
                WifiListPage(
                    bottomBaronClickedActions = bottomBaronClickedActions,
                    moveToFirstMainPage = {
                        centerButtonTarget = "MainPage"
                        navController.navigate("MainPage")
                    },
                    pinnedWifiName = pinnedWifiName,
                    setPinnedWifiName = { pinnedWifiName = it },
                    navToHelpPage = { navController.navigate("HelpPage") }

                )
            }

            composable(route = "GetArmLengthPage") {
                GetArmLengthPage(
                    navigationBack = navigationBack,
                    moveToGetAllDistancePage = { navController.navigate("GetAllDistancePage") },
                    pinnedWifiName = pinnedWifiName,
                    navToHelpPage = { navController.navigate("HelpPage") }

                )
            }
            composable(route = "GetAllDistancePage") {
                GetAllDistancePage(
                    navigationBack = navigationBack,
                    navToOneDistancePage = listOf(
                        { navController.navigate("GetUpDistancePage") },
                        { navController.navigate("GetDownDistancePage") },
                        { navController.navigate("GetLeftDistancePage") },
                        { navController.navigate("GetFrontDistancePage") },
                    ),
                    pinnedWifiName = pinnedWifiName,
                    navToHelpPage = { navController.navigate("HelpPage") }

                )
            }


            composable(route = "GetUpDistancePage") {
                GetOneDistancePage(
                    navigationBack = { navController.navigateUp() },
                    distance = "1.99m",
                    imageResId = R.drawable.up,
                    pinnedWifiName = pinnedWifiName,
                    navToHelpPage = { navController.navigate("HelpPage") }

                )
            }
            composable(route = "GetDownDistancePage") {
                GetOneDistancePage(
                    navigationBack = { navController.navigateUp() },
                    distance = "1.23m",
                    imageResId = R.drawable.down,
                    pinnedWifiName = pinnedWifiName,
                    navToHelpPage = { navController.navigate("HelpPage") }

                )
            }
            composable(route = "GetLeftDistancePage") {
                GetOneDistancePage(
                    navigationBack = { navController.navigateUp() },
                    distance = "1.213m",
                    imageResId = R.drawable.left,
                    pinnedWifiName = pinnedWifiName,
                    navToHelpPage = { navController.navigate("HelpPage") }

                )
            }
            composable(route = "GetFrontDistancePage") {
                GetOneDistancePage(
                    navigationBack = { navController.navigateUp() },
                    distance = "2m",
                    imageResId = R.drawable.front,
                    pinnedWifiName = pinnedWifiName,
                    navToHelpPage = { navController.navigate("HelpPage") }
                )
            }

            composable(route = "LoadingPage") {
                LoadingPage(
                    text = "주변 네트워크를 검색하고 있어요.\n대략 30~40초 정도 걸려요.",
                    navController = navController
                )
            }
            composable(route = "HelpPage") {
                HelpPage(navigationBack = navigationBack)
            }

        }
    }
}
