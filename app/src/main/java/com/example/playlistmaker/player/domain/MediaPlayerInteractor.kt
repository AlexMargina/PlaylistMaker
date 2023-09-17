package com.example.playlistmaker.player.domain


import com.example.playlistmaker.player.domain.PlayerState

interface MediaPlayerInteractor {

    var playerState: PlayerState
    fun preparePlayer(
        dataSource: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    )

    fun playbackControl(onStartPlayer: () -> Unit, onPausePlayer: () -> Unit)
    fun startPlayer(startPlayer: () -> Unit)
    fun pausePlayer(pausePlayer: () -> Unit)
    fun release()
    fun currentPosition(): Int
}