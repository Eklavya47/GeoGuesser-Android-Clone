package com.betanooblabs.geoguesserandroidclone.screens.ui

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.betanooblabs.geoguesserandroidclone.R
import com.betanooblabs.geoguesserandroidclone.screens.components.CustomButton
import com.betanooblabs.geoguesserandroidclone.screens.viewModel.GuessPlaceScreenViewModel
import com.betanooblabs.geoguesserandroidclone.ui.theme.custom_blue
import com.betanooblabs.geoguesserandroidclone.ui.theme.custom_yellow
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.StreetViewPanorama
import com.google.android.gms.maps.StreetViewPanoramaView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun GuessPlaceScreen(
    viewModel: GuessPlaceScreenViewModel,
    onViewSummaryClick: () -> Unit,
) {
    val isMapOpen = remember { mutableStateOf(false) }
    val isConfirmed by viewModel.isConfirmed
    val score by viewModel.score
    val distance by viewModel.distanceMiles
    val round by viewModel.roundIndex
    val actualLocation = viewModel.actualLocation
    val isGameOver = viewModel.isGameOver

    Box(modifier = Modifier.fillMaxSize()) {

        StreetViewContainer(
            latitude = actualLocation.latitude,
            longitude = actualLocation.longitude,
            modifier = Modifier.fillMaxSize()
        )

        AnimatedVisibility(
            visible = isConfirmed && score != null && distance != null,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it },
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            ScoreBoard(
                round = round+1,
                score = score ?: 0,
                isGameOver = isGameOver,
                distanceMiles = distance ?: 0.0,
                onNextRoundClick = {
                    if (viewModel.isGameOver){
                        onViewSummaryClick()
                    } else{
                        isMapOpen.value = false
                        viewModel.nextRound()
                    }

                }
            )
        }

        FloatingActionButton(
            onClick = {
                isMapOpen.value = true
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp),
            containerColor = Color.Black
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_map),
                contentDescription = "Open Map",
                tint = Color.White
            )
        }

        // Map Bottom Sheet
        AnimatedVisibility(
            visible = isMapOpen.value,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            MapBottomSheet(
                viewModel = viewModel,
                onClose = { isMapOpen.value = false },
            )
        }
    }
}

@Composable
fun StreetViewContainer(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    var panorama by remember { mutableStateOf<StreetViewPanorama?>(null) }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            StreetViewPanoramaView(context).apply {
                onCreate(null)
                getStreetViewPanoramaAsync {
                    panorama = it
                    panorama!!.isUserNavigationEnabled = true
                    panorama!!.isZoomGesturesEnabled = true
                    panorama!!.isPanningGesturesEnabled = true
                    panorama!!.isStreetNamesEnabled = false
                }
            }
        },
        update = {
            panorama?.setPosition(LatLng(latitude, longitude))
        }
    )
}

@Composable
fun MapBottomSheet(
    viewModel: GuessPlaceScreenViewModel,
    onClose: () -> Unit,
) {
    //val userGuess by viewModel.userGuess
    val isConfirmed by viewModel.isConfirmed

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .background(
                Color.Black,
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
    ) {

        // Google Map
        MapContainer(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel
        )

        // Close icon (top-right)
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                //.background(Color.Black.copy(alpha = 0.6f), CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.close_button),
                contentDescription = "Close Map",
                tint = Color.Black
            )
        }

        // Confirm button (bottom-center)
        IconButton(
            onClick = { viewModel.confirmGuess() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
        ) {
            Icon(
                painter = painterResource(
                    if (isConfirmed)
                        R.drawable.mark_button
                    else
                        R.drawable.mark_button_transparent
                ),
                contentDescription = "Confirm Location",
                tint = Color.Black
            )
        }

        /*FloatingActionButton(
            onClick = { viewModel.confirmGuess() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp),
            containerColor = Color.Transparent
        ) {
            Icon(
                painter = painterResource(if (isConfirmed)R.drawable.mark_button else R.drawable.mark_button_transparent),
                contentDescription = "Confirm Location",
                //tint = Color.White
            )
        }*/
    }
}

@Composable
fun MapContainer(
    modifier: Modifier = Modifier,
    viewModel: GuessPlaceScreenViewModel
) {
    val context = LocalContext.current
    val userGuess by viewModel.userGuess
    val isConfirmed by viewModel.isConfirmed
    val actualLocation = viewModel.actualLocation
    val actualMarkerState = rememberUpdatedMarkerState(position = actualLocation)
    val shouldAnimateCamera by viewModel.shouldAnimateCamera
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
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            compassEnabled = false,
            myLocationButtonEnabled = false
        ),
        onMapClick = { latLng ->
            if (!isConfirmed){
                viewModel.onMapClick(latLng)
            }
        }
    ) {
        // User guess marker (red)
        userGuess?.let {
            Marker(
                state = MarkerState(position = it),
                icon = redMarkerIcon
            )
        }

        // Actual location marker + polyline (after confirm)
        if (isConfirmed && userGuess != null) {

            Marker(
                state = actualMarkerState,
                icon = blueMarkerIcon
            )

            Polyline(
                points = listOf(userGuess!!, actualLocation),
                color = Color.Blue,
                width = 8f,
                pattern = listOf(Dash(30f), Gap(20f))
            )
        }

        // Camera animation AFTER confirm
        LaunchedEffect(shouldAnimateCamera) {
            if (shouldAnimateCamera && userGuess != null) {

                val bounds = LatLngBounds.builder()
                    .include(userGuess!!)
                    .include(actualLocation)
                    .build()

                cameraPositionState.animate(
                    update = CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        120 // padding in px
                    ),
                    durationMs = 1000
                )

                viewModel.onCameraAnimationDone()
            }
        }
    }
}

@Composable
fun ScoreBoard(
    round: Int,
    score: Int,
    isGameOver: Boolean,
    distanceMiles: Double,
    onNextRoundClick: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }

    val animatedScore by animateIntAsState(
        targetValue = if (startAnimation) score else 0,
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        ),
        label = ""
    )

    val animatedProgress by animateFloatAsState(
        targetValue = if (startAnimation) score / 5000f else 0f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        ),
        label = ""
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f) // only cover street view portion
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

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_location_on),
                contentDescription = null,
                //tint = Color.Blue,
                modifier = Modifier.size(40.dp).padding(top = 10.dp)
            )

            Text(
                text = "Round $round",
                color = Color.White,
                fontSize = 20.sp
            )

            Text(
                text = "You got $animatedScore points",
                color = custom_yellow,
                fontSize = 18.sp
            )

            /*LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50)),
                color = custom_yellow,
                trackColor = Color.DarkGray,
                strokeCap = StrokeCap.Butt,
                drawStopIndicator = {}
            )*/

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.DarkGray)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .background(custom_yellow)
                )
            }

            Text(
                text = "You were %.2f miles away".format(distanceMiles),
                color = Color.White,
                fontSize = 16.sp
            )

            CustomButton(
                if (isGameOver) "View Summary" else "Next Round",
                custom_blue,
                Color.Black,
                modifier = Modifier.fillMaxWidth(),
                onNextRoundClick
            )
        }
    }
}




