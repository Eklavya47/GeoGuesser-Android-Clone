package com.betanooblabs.geoguesserandroidclone.screens.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betanooblabs.geoguesserandroidclone.screens.components.BlueBorderButton
import com.betanooblabs.geoguesserandroidclone.screens.components.GifImage

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun HomeScreen(
    onFirstClick: () -> Unit,
    onSecondClick: () -> Unit,
    onThirdClick: () -> Unit,
    innerPadding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .background(Color.Black)
    ) {

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter),
                //.padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // GIF at top center
            GifImage(
                modifier = Modifier
                    .size(360.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons column
            Column(
                modifier = Modifier.padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                BlueBorderButton(text = "GUESS THE PLACE", onFirstClick)
                BlueBorderButton(text = "GUESS THE COUNTRY", onSecondClick)
                BlueBorderButton(text = "PLAY WITH FRIENDS", onThirdClick)
            }
        }

        // Bottom center text
        Text(
            text = "Geo Guesser",
            color = Color.White,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}