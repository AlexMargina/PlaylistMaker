package com.example.playlistmaker.player.domain

import com.example.playlistmaker.search.domain.TrackModel

interface MediaPlayerRepository {

    fun preparePlayer(url: String, onPreparedListener: () -> Unit)

    fun setOnCompletionListener(onCompletionListener: () -> Unit)

    fun currentPosition(): Int

    fun startPlayer()

    fun pausePlayer()

    fun destroyPlayer()

    fun getTrack() : TrackModel

    fun isNightTheme() : Boolean
}