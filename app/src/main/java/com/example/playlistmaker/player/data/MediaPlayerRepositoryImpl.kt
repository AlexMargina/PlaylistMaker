package com.example.playlistmaker.player.data

import android.app.Application
import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.domain.TrackModel
import com.example.playlistmaker.sharing.data.SharedPrefsUtils
import com.example.playlistmaker.sharing.domain.App
import com.example.playlistmaker.sharing.domain.App.Companion.historyTracks

@Suppress("CAST_NEVER_SUCCEEDS")
class MediaPlayerRepositoryImpl() : MediaPlayerRepository {

     val mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String, onPreparedListener: () -> Unit) {
        val sourse = historyTracks[0].previewUrl
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

    override fun getTrack() : TrackModel {
        val track: TrackModel
        if (App.historyTracks.size>0) {
            track = App.historyTracks[0]

        } else {
            val app:Application = App()
            val sharedPrefs = SharedPrefsUtils(App as Application) ///не знаю что сюда уже передать!
            val historyTracks = sharedPrefs.readClickedSearchSongs().map {
                TrackModel(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
            track = historyTracks[0]
        }
         return track
    }

    override fun isNightTheme() : Boolean {
        return App.darkTheme
    }

}