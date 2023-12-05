package com.example.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val mediaPlayerInteractor: MediaPlayerInteractor) : ViewModel() {

    private var timerJob: Job? = null
    private var favoriteJob: Job? = null
    private val RefreshDelayMs = 300L

    private val _playerState = MutableLiveData<PlayerState>(PlayerState.DEFAULT())
    fun observePlayerState(): LiveData<PlayerState> = _playerState

    init {
        prepareMediaPlayer()
        setCompletionMediaPlayer()
    }

    private fun prepareMediaPlayer() {
        mediaPlayerInteractor.preparePlayer(getTrack().previewUrl) {
            _playerState.postValue(PlayerState.PREPARED())
        }
    }

    private fun setCompletionMediaPlayer() {
        mediaPlayerInteractor.setOnCompletionListener {
            timerJob?.cancel()
            mediaPlayerInteractor.stopPlayer()
            prepareMediaPlayer()
        }
    }

    fun getTrack(): TrackModel {
        return mediaPlayerInteractor.getTrack()
    }

    suspend fun saveFavoriteTrack (track: TrackModel) {
        mediaPlayerInteractor.saveTrack(track)
    }
    private fun getCurrentPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(mediaPlayerInteractor.currentPosition()) ?: "00:00"
    }

    fun isNightTheme(): Boolean {
        return mediaPlayerInteractor.isNightTheme()
    }

    private fun startAudioPlayer() {
        mediaPlayerInteractor.startPlayer()
        _playerState.postValue(PlayerState.PLAYING(getCurrentPosition()))
        startTimer()
    }

    private fun pauseAudioPlayer() {
        mediaPlayerInteractor.pausePlayer()
        timerJob?.cancel()
        _playerState.postValue(PlayerState.PAUSED(getCurrentPosition()))
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            delay(RefreshDelayMs)
            while (_playerState.value is PlayerState.PLAYING) {
                delay(RefreshDelayMs)
                _playerState.postValue(PlayerState.PLAYING(getCurrentPosition()))
            }
        }
    }


    fun playbackControl() {
        when (_playerState.value) {
            is PlayerState.PLAYING -> {
                pauseAudioPlayer()
            }

            is PlayerState.PREPARED -> {
                startAudioPlayer()
            }

            is PlayerState.PAUSED -> {
                startAudioPlayer()
            }

            else -> {}
        }
    }

     fun likeOrDislike() {
        val playedTrack = getTrack()
        favoriteJob = viewModelScope.launch {
            if (playedTrack.isFavorite) {
                playedTrack.isFavorite = false
                deleteTrackFromFavorite(playedTrack.trackId)
            }  else {
                playedTrack.isFavorite = true
                insertTrackToFavorite(playedTrack)
            }
        }
     }

    suspend fun insertTrackToFavorite(track: TrackModel) {
        mediaPlayerInteractor.insertTrackToFavorite(track)
    }

     suspend fun deleteTrackFromFavorite(trackId: String) {
         mediaPlayerInteractor.deleteTrackFromFavorite(trackId)
    }

    override fun onCleared() {
        mediaPlayerInteractor.destroyPlayer()
    }
}