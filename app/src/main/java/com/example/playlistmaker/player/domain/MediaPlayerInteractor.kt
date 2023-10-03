package com.example.playlistmaker.player.domain


interface MediaPlayerInteractor {

    fun preparePlayer(url: String, onPreparedListener: () -> Unit)

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun currentPosition(): Int

    fun startPlayer()

    fun pausePlayer()

    fun destroyPlayer()

    fun getTrack() : TrackPlayerModel

    fun isNightTheme() : Boolean
}