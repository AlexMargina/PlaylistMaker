package com.example.playlistmaker.player.domain

sealed interface PlayerState {
    object DEFAULT : PlayerState
    object PREPARED : PlayerState
    data class PLAYING(
        val time: Int
    ) : PlayerState

    object PAUSED : PlayerState
}