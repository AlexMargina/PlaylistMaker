package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.domain.TrackSearchModel
import com.example.playlistmaker.sharing.domain.App
import com.example.playlistmaker.sharing.domain.App.Companion.historyTracks

@Suppress("CAST_NEVER_SUCCEEDS")
class MediaPlayerRepositoryImpl() : MediaPlayerRepository {

//    val historyTracks = SharedPrefsSearchDataStorage(context = Context).getSearchHistory()
//        .map {
//        TrackSearchModel(
//            it.trackId,
//            it.trackName,
//            it.artistName,
//            it.trackTimeMillis,
//            it.artworkUrl100,
//            it.collectionName,
//            it.releaseDate,
//            it.primaryGenreName,
//            it.country,
//            it.previewUrl
//        )
//    }


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

    override fun getTrack() : TrackSearchModel {

        return historyTracks[0]
    }

    override fun isNightTheme() : Boolean {
        return App.darkTheme
    }

}