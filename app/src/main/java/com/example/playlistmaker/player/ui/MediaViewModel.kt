package com.example.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.player.domain.TrackPlayerModel
import java.text.SimpleDateFormat
import java.util.Locale

class MediaViewModel    (
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

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

    fun getTrack() : TrackPlayerModel {
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

            else -> {}
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
        pauseAudioPlayer()
        handler.removeCallbacksAndMessages(updateTime())
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



    companion object{
        fun getViewModelFactory(): ViewModelProvider.Factory = object: ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T: ViewModel> create (modelClass: Class<T>):T{
                return MediaViewModel(
                    Creator.provideMediaPlayerInteractor()
                ) as T
            }
        }
    }
}