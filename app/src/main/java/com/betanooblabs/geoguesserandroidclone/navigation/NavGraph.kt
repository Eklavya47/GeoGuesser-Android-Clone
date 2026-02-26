package com.betanooblabs.geoguesserandroidclone.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.betanooblabs.geoguesserandroidclone.screens.ui.GuessPlaceScreen
import com.betanooblabs.geoguesserandroidclone.screens.ui.HomeScreen
import com.betanooblabs.geoguesserandroidclone.screens.ui.SummaryScreen
import com.betanooblabs.geoguesserandroidclone.screens.viewModel.GuessPlaceScreenViewModel

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

        composable(Screen.GuessPlace.route) {backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.GuessPlace.route)
            }

            val viewModel: GuessPlaceScreenViewModel = viewModel(parentEntry)

            GuessPlaceScreen(
                viewModel = viewModel,
                onViewSummaryClick = {
                    navController.navigate(Screen.Summary.route)
                }
            )
        }

        composable(Screen.ScreenTwo.route) {
            //ScreenTwo()
        }

        composable(Screen.ScreenThree.route) {
            //ScreenThree()
        }

        composable(Screen.Summary.route) {backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Screen.GuessPlace.route)
            }

            val viewModel: GuessPlaceScreenViewModel = viewModel(parentEntry)

            SummaryScreen(
                viewModel = viewModel,

                onPlayAgain = {
                    navController.navigate(Screen.GuessPlace.route) {
                        popUpTo(Screen.GuessPlace.route) {
                            inclusive = true
                        }
                    }
                },

                onMainMenu = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.GuessPlace.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
