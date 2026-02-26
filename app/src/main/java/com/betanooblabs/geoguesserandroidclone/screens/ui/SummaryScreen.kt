package com.betanooblabs.geoguesserandroidclone.screens.ui

import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betanooblabs.geoguesserandroidclone.R
import com.betanooblabs.geoguesserandroidclone.model.RoundResult
import com.betanooblabs.geoguesserandroidclone.screens.components.CustomButton
import com.betanooblabs.geoguesserandroidclone.screens.viewModel.GuessPlaceScreenViewModel
import com.betanooblabs.geoguesserandroidclone.ui.theme.custom_blue
import com.betanooblabs.geoguesserandroidclone.ui.theme.custom_yellow
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun SummaryScreen(
    viewModel: GuessPlaceScreenViewModel,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit
) {
    BackHandler(enabled = true) {
        // Block system back
    }

    val totalScore by viewModel.totalScore
    val roundResults = viewModel.roundResults

    val totalDistance = remember(roundResults) {
        roundResults.sumOf { it.distanceMiles }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SummaryMap(
            roundResults = roundResults,
            modifier = Modifier.weight(1f)
        )

        SummaryContent(
            roundResults = roundResults,
            totalScore = totalScore,
            totalDistance = totalDistance,
            modifier = Modifier.weight(1f),
            onPlayAgain = onPlayAgain,
            onMainMenu = onMainMenu
        )
    }
}

@Composable
fun SummaryMap(
    roundResults: List<RoundResult>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val blueMarkerIcon = remember(R.drawable.blue_pin) {
        BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.blue_pin)
        )
    }
    val redMarkerIcon = remember(R.drawable.red_pin) {
        BitmapDescriptorFactory.fromBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.red_pin)
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = false,
            myLocationButtonEnabled = false
        )
    ) {

        roundResults.forEach { result ->

            Marker(
                state = rememberUpdatedMarkerState(position = result.guessedLocation),
                icon = redMarkerIcon
            )

            Marker(
                state = rememberUpdatedMarkerState(position = result.correctLocation),
                icon = blueMarkerIcon
            )

            Polyline(
                points = listOf(
                    result.guessedLocation,
                    result.correctLocation
                ),
                color = Color.Blue,
                width = 8f,
                pattern = listOf(Dash(30f), Gap(20f))
            )
        }
    }
}

@Composable
fun SummaryContent(
    roundResults: List<RoundResult>,
    totalScore: Int,
    totalDistance: Double,
    modifier: Modifier = Modifier,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color(0xFF121212),
                        Color(0xFF1A1A1A)
                    )
                )
            )
            .padding(16.dp)
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_location_on),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(36.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "GAME SUMMARY",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            itemsIndexed(roundResults) { index, result ->

                SummaryRow(
                    index = index + 1,
                    distance = result.distanceMiles,
                    score = result.score
                )
                if (index < roundResults.lastIndex) {
                    HorizontalDivider(
                        color = Color.DarkGray,
                        thickness = 1.dp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text("Total",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = String.format("%.2f miles", totalDistance),
                color = custom_yellow,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$totalScore points",
                color = custom_yellow,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            CustomButton (
                "PLAY AGAIN",
                Color.Transparent,
                Color.Green,
                modifier = Modifier.weight(1f),
                onPlayAgain
            )

            CustomButton (
                "MAIN MENU",
                custom_blue,
                Color.Black,
                modifier = Modifier.weight(1f),
                onMainMenu
            )
        }
    }
}

@Composable
fun SummaryRow(
    index: Int,
    distance: Double,
    score: Int
) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text("$index", color = Color.White)

        Text(
            text = String.format("%.2f miles", distance),
            color = Color.White
        )

        Text(
            text = "$score points",
            color = Color.White
        )
    }
}