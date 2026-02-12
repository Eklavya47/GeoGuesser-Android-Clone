package com.betanooblabs.geoguesserandroidclone

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.betanooblabs.geoguesserandroidclone.navigation.AppNavGraph
import com.betanooblabs.geoguesserandroidclone.screens.ui.HomeScreen
import com.betanooblabs.geoguesserandroidclone.ui.theme.GeoGuesserAndroidCloneTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(SystemBarStyle.dark(
                scrim = Color.Black.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.dark(
                scrim = Color.Black.toArgb()
            )
        )
        setContent {
            val navController = rememberNavController()
            GeoGuesserAndroidCloneTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavGraph(innerPadding, navController)
                }
            }
        }
    }
}