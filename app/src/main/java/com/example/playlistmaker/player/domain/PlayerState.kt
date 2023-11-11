package com.example.playlistmaker.player.domain

sealed class PlayerState(val isPlayButtonEnabled: Boolean, val buttonText: String, val progress: String) {

    class DEFAULT : PlayerState(false, "PLAY", "00:00")

    class PREPARED : PlayerState(true, "PLAY", "00:00")

    class PLAYING(progress: String) : PlayerState(true, "PAUSE", progress)

    class PAUSED(progress: String) : PlayerState(true, "PLAY", progress)

}