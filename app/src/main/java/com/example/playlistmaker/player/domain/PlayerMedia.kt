package com.example.playlistmaker.player.domain

import android.media.MediaPlayer


class PlayerMedia ( ) {

    companion object  {
        val STATE_DEFAULT = 0
        val STATE_PREPARED = 1
        val STATE_PLAYING = 2
        val STATE_PAUSED = 3
    }

    var mediaPlayer = MediaPlayer()
    var playerState  = STATE_DEFAULT

     fun preparePlayer(trackViewUrl: String) {
        mediaPlayer.setDataSource(trackViewUrl.toString())
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {

            playerState = STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }


    fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }


    fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }


}