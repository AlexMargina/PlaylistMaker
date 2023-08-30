package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository

class MediaPlayerInteractorImpl (private val mediaPlayerRepository: MediaPlayerRepository) :
    MediaPlayerInteractor {
    override fun preparePlayer(
        dataSource: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun playbackControl(onStartPlayer: () -> Unit, onPausePlayer: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun startPlayer(startPlayer: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun pausePlayer(pausePlayer: () -> Unit) {
        TODO("Not yet implemented")
    }

    override fun release() {
        TODO("Not yet implemented")
    }

    override fun currentPosition(): Int {
        TODO("Not yet implemented")
    }

}