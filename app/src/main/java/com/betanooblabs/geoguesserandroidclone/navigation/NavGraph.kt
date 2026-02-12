package com.betanooblabs.geoguesserandroidclone.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.betanooblabs.geoguesserandroidclone.screens.ui.GuessPlaceScreen
import com.betanooblabs.geoguesserandroidclone.screens.ui.HomeScreen

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AppNavGraph(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(Screen.Home.route) {
            HomeScreen(
                onFirstClick = {
                    navController.navigate(Screen.GuessPlace.route)
                },
                onSecondClick = {
                    navController.navigate(Screen.ScreenTwo.route)
                },
                onThirdClick = {
                    navController.navigate(Screen.ScreenThree.route)
                },
                innerPadding
            )
        }

        composable(Screen.GuessPlace.route) {
            GuessPlaceScreen()
        }

        composable(Screen.ScreenTwo.route) {
            //ScreenTwo()
        }

        composable(Screen.ScreenThree.route) {
            //ScreenThree()
        }
    }
}
