package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.App
import com.example.playlistmaker.App.Companion.historyTracks
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.domain.TrackModel

@Suppress("CAST_NEVER_SUCCEEDS")
class MediaPlayerRepositoryImpl(private val mediaPlayer : MediaPlayer) : MediaPlayerRepository {

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

        if (App.historyTracks.isNullOrEmpty()) {
            val defaultTracks =  arrayListOf<TrackModel>((
                    TrackModel(1,"Выберите свою песню", "Я",100,  "a.a", "a", "2023", "a", "RU", "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview122/v4/80/69/1b/80691ba9-30bb-2c5b-929c-55dab79ed6eb/mzaf_14196615094672459806.plus.aac.p.m4a")))
            historyTracks.add(defaultTracks[0])
        }
         return historyTracks[0]
    }

    override fun isNightTheme() : Boolean {
        return App.darkTheme
    }
}