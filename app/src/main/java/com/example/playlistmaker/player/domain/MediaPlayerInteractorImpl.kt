package com.example.playlistmaker.player.domain

import com.example.playlistmaker.creator.Creator

class MediaPlayerInteractorImpl() :
    MediaPlayerInteractor {

    val mediaPlayerRepository = Creator.provideMediaPlayerRepository()
    override fun preparePlayer(url: String, onPreparedListener: () -> Unit) {
        mediaPlayerRepository.preparePlayer(url, onPreparedListener)
    }

    override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        mediaPlayerRepository.setOnCompletionListener(onCompletionListener)
    }

    override fun currentPosition(): Int {
        return mediaPlayerRepository.currentPosition()
    }

    override fun startPlayer() {
        mediaPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        mediaPlayerRepository.pausePlayer()
    }

    override fun destroyPlayer() {
        mediaPlayerRepository.destroyPlayer()
    }

    override fun getTrack() : TrackPlayerModel {
        return mediaPlayerRepository.getTrack()
    }

    override fun isNightTheme() : Boolean {
        return mediaPlayerRepository.isNightTheme()
    }
}



