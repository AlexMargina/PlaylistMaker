package com.example.playlistmaker.player.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.TrackModel
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel   (application: Application): AndroidViewModel(application)
 {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MediaViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()
    private val handler = Handler(Looper.getMainLooper())

    private var clickAllowed = true
    private val RefreshDelayMs = 333L
    private val ClickDelayMs = 1000L
    private val _stateLiveData = MutableLiveData<PlayerState>()
    private val _timerLiveData = MutableLiveData<String>()

    fun observatorScreen(): LiveData<PlayerState> = _stateLiveData
    fun observatorTimer(): LiveData<String> = _timerLiveData

    init {
        updateState(PlayerState.DEFAULT)
        prepareAudioPlayer()
        setOnCompleteListener()
        isClickAllowed()
    }

    fun getTrack() : TrackModel {
        return mediaPlayerInteractor.getTrack()
    }

    fun isNightTheme() : Boolean {
        return mediaPlayerInteractor.isNightTheme()
    }

    private fun prepareAudioPlayer() {
        mediaPlayerInteractor.preparePlayer(getTrack().previewUrl) {
            updateState(PlayerState.PREPARED)
        }
    }


    private fun startAudioPlayer() {
        mediaPlayerInteractor.startPlayer()
        updateState(PlayerState.PLAYING(mediaPlayerInteractor.currentPosition()))
    }

    private fun pauseAudioPlayer() {
        mediaPlayerInteractor.pausePlayer()
        updateState(PlayerState.PAUSED)
    }


    private fun getCurrentPosition(): Int {
        return mediaPlayerInteractor.currentPosition()
    }

    private fun setOnCompleteListener() {
        mediaPlayerInteractor.setOnCompletionListener {
            updateState(PlayerState.PREPARED)
        }
    }

    fun playbackControl() {
        when (_stateLiveData.value) {
            is PlayerState.PLAYING -> {
                pauseAudioPlayer()
            }

            is PlayerState.PREPARED, PlayerState.PAUSED -> {
                startAudioPlayer()
                handler.post(updateTime())
            }

            else -> {prepareAudioPlayer()}
        }
    }

    private fun updateState(state: PlayerState) {
        _stateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(null)
        mediaPlayerInteractor.destroyPlayer()
    }

    fun onPause() {
        handler.removeCallbacksAndMessages(updateTime())
        mediaPlayerInteractor.pausePlayer()
        updateState(PlayerState.PAUSED)
    }

    private fun updateTime(): Runnable {
        return object : Runnable {
            override fun run() {
                _timerLiveData.postValue(
                    SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(getCurrentPosition())
                )
                handler.postDelayed(this, RefreshDelayMs)
            }
        }
    }

    fun isClickAllowed(): Boolean {
        val current = clickAllowed
        if (clickAllowed) {
            clickAllowed = false
            handler.postDelayed({ clickAllowed = true }, ClickDelayMs)
        }
        return current
    }


}