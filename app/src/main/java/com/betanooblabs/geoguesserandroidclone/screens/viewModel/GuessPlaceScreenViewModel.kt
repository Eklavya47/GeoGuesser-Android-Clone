package com.betanooblabs.geoguesserandroidclone.screens.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.betanooblabs.geoguesserandroidclone.Constants
import com.betanooblabs.geoguesserandroidclone.model.RoundResult
import com.google.android.gms.maps.model.LatLng

class GuessPlaceScreenViewModel : ViewModel() {

    private val roundPlaces = Constants.getRandomFivePlaces()

    private val _roundIndex = mutableStateOf(0)
    val roundIndex: State<Int> = _roundIndex

    val totalRounds = 5

    val actualLocation: LatLng
        get() = roundPlaces[_roundIndex.value]

    private val _userGuess = mutableStateOf<LatLng?>(null)
    val userGuess: State<LatLng?> = _userGuess

    private val _isConfirmed = mutableStateOf(false)
    val isConfirmed: State<Boolean> = _isConfirmed

    private val _shouldAnimateCamera = mutableStateOf(false)
    val shouldAnimateCamera: State<Boolean> = _shouldAnimateCamera

    private val _distanceMiles = mutableStateOf<Double?>(null)
    val distanceMiles: State<Double?> = _distanceMiles

    private val _score = mutableStateOf<Int?>(null)
    val score: State<Int?> = _score

    /*private val _round = mutableIntStateOf(1)
    val round: State<Int> = _round*/

    private val _totalScore = mutableIntStateOf(0)
    val totalScore: State<Int> = _totalScore

    val isGameOver: Boolean
        get() = _roundIndex.value == totalRounds - 1

    private val _roundResults = mutableStateListOf<RoundResult>()
    val roundResults: List<RoundResult> = _roundResults

    fun nextRound() {

        if (_roundIndex.value < totalRounds - 1) {
            _roundIndex.value += 1
            resetRound()
        }
    }

    fun onCameraAnimationDone() {
        _shouldAnimateCamera.value = false
    }

    fun onMapClick(latLng: LatLng) {
        if (_isConfirmed.value) return
        _userGuess.value = latLng
    }

    fun confirmGuess() {
        if (_userGuess.value != null) {
            _isConfirmed.value = true
            _shouldAnimateCamera.value = true

            val distance = calculateDistanceMiles(userGuess.value!!, actualLocation)
            val roundScore = calculateScore(distance)

            _distanceMiles.value = distance
            _score.value = roundScore

            _totalScore.intValue += roundScore

            _roundResults.add(
                RoundResult(
                    guessedLocation = _userGuess.value!!,
                    correctLocation = actualLocation,
                    score = roundScore,
                    distanceMiles = distance
                )
            )
        }
    }

    fun calculateDistanceMiles(
        start: LatLng,
        end: LatLng
    ): Double {

        val earthRadiusMiles = 3958.8

        val dLat = Math.toRadians(end.latitude - start.latitude)
        val dLng = Math.toRadians(end.longitude - start.longitude)

        val a = kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2) +
                kotlin.math.cos(Math.toRadians(start.latitude)) *
                kotlin.math.cos(Math.toRadians(end.latitude)) *
                kotlin.math.sin(dLng / 2) *
                kotlin.math.sin(dLng / 2)

        val c = 2 * kotlin.math.atan2(
            kotlin.math.sqrt(a),
            kotlin.math.sqrt(1 - a)
        )

        return earthRadiusMiles * c
    }

    fun calculateScore(distanceMiles: Double): Int {

        val maxScore = 5000

        // Exponential decay formula
        val score = maxScore * kotlin.math.exp(-distanceMiles / 2000)

        return score.toInt().coerceAtLeast(0)
    }

    private fun resetRound() {
        _userGuess.value = null
        _isConfirmed.value = false
        _distanceMiles.value = null
        _score.value = null
        _shouldAnimateCamera.value = false
    }
}
