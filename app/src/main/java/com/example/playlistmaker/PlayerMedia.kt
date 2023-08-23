package com.example.playlistmaker

import android.media.MediaPlayer
import android.net.Uri
import android.widget.Button

import android.widget.ImageView


class PlayerMedia ( var buttonPlay : Button) {

    companion object  {
        val STATE_DEFAULT = 0
        val STATE_PREPARED = 1
        val STATE_PLAYING = 2
        val STATE_PAUSED = 3
    }

    var mediaPlayer = MediaPlayer()
    private var playerState  = STATE_DEFAULT

     fun preparePlayer(trackViewUrl: String) {
        mediaPlayer.setDataSource(trackViewUrl.toString())
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlay.isEnabled = true
            playerState = STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {

            playerState = STATE_PREPARED
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        buttonPlay.text = "PAUSE"
        playerState = STATE_PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        buttonPlay.text = "PLAY"
        playerState = STATE_PAUSED
    }


    private fun playbackControl() {
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