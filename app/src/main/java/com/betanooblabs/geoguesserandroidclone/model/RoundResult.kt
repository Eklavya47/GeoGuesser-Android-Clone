package com.betanooblabs.geoguesserandroidclone.model

import com.google.android.gms.maps.model.LatLng

data class RoundResult(
    val guessedLocation: LatLng,
    val correctLocation: LatLng,
    val score: Int,
    val distanceMiles: Double
)
