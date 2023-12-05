package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.App
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.data.SearchRepositoryImpl.Companion.clickedTracks
import com.example.playlistmaker.search.domain.TrackModel

class MediaPlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer
) : MediaPlayerRepository {

    override val isPlaying = mediaPlayer.isPlaying

    override fun preparePlayer(url: String, onPreparedListener: () -> Unit) {
        val track = clickedTracks[0].previewUrl
        mediaPlayer.setDataSource(track)
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

    override fun stopPlayer() {
        mediaPlayer.reset()
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
    }

    override fun getTrack(): TrackModel {
        return clickedTracks[0]
    }

    override fun isNightTheme(): Boolean {
        return App.darkTheme
    }


}