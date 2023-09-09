package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository

class MediaPlayerInteractorImpl (private val mediaPlayerRepository: MediaPlayerRepository) :
    MediaPlayerInteractor {

    override var playerState = PlayerState.DEFAULT
    override fun preparePlayer(
        previewUrl: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        mediaPlayerRepository.apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                onPreparedListener()
                playerState = PlayerState.PREPARED
            }
            setOnCompletionListener {
                onCompletionListener()
                playerState = PlayerState.PREPARED
            }
        }
    }

    override fun startPlayer(startPlayer: () -> Unit) {
        startPlayer()
        mediaPlayerRepository.start()
        playerState = PlayerState.PLAYING
    }

    override fun pausePlayer(pausePlayer: () -> Unit) {
        pausePlayer()
        mediaPlayerRepository.pause()
        playerState = PlayerState.PAUSED
    }

    override fun release() {
        mediaPlayerRepository.release()
    }

    override fun currentPosition(): Int {
        return mediaPlayerRepository.currentPosition()
    }


    override fun playbackControl(onStartPlayer: () -> Unit, onPausePlayer: () -> Unit) {
        when (playerState) {
            PlayerState.PLAYING -> {
                onPausePlayer()
                pausePlayer(onPausePlayer)
                playerState = PlayerState.PAUSED
            }
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                onStartPlayer()
                startPlayer(onStartPlayer)
                playerState = PlayerState.PLAYING
            }
            PlayerState.DEFAULT -> {}
        }
    }
}

enum class PlayerState {
    DEFAULT, PREPARED, PLAYING, PAUSED;

}