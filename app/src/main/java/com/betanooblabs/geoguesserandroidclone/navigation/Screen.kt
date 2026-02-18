package com.betanooblabs.geoguesserandroidclone.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object GuessPlace : Screen("guess_place")
    object ScreenTwo : Screen("screen_two")
    object ScreenThree : Screen("screen_three")
    object Summary : Screen("summary")
}