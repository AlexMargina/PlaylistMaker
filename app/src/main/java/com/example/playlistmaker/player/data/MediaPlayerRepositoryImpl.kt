package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.player.domain.TrackPlayerModel
import com.example.playlistmaker.sharing.domain.App
import com.example.playlistmaker.sharing.domain.App.Companion.playedTracks as playedTracks1

@Suppress("CAST_NEVER_SUCCEEDS")
class MediaPlayerRepositoryImpl() : MediaPlayerRepository {


     val mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String, onPreparedListener: () -> Unit) {
        val sourse = App.playedTracks[0].previewUrl.toString()
        mediaPlayer.setDataSource(sourse)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPreparedListener()
        }
    }

    override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        mediaPlayer.setOnCompletionListener { onCompletionListener() }
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }

    override fun getTrack() : TrackPlayerModel {
        return playedTracks1[0] as TrackPlayerModel
    }

    override fun isNightTheme() : Boolean {
        return App.darkTheme
    }

}