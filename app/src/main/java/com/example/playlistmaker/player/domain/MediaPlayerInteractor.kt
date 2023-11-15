package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.TrackModel


interface MediaPlayerInteractor {

    val isPlaying : Boolean

    fun preparePlayer(url: String, onPreparedListener: () -> Unit)

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun currentPosition(): Int

    fun startPlayer()

    fun pausePlayer()

    fun stopPlayer()

    fun destroyPlayer()

    fun getTrack() : TrackModel

    fun isNightTheme() : Boolean
}