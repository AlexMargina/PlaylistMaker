package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.App
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.convertor.TrackDbConvertor
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.data.SearchRepositoryImpl.Companion.clickedTracks
import com.example.playlistmaker.search.domain.TrackModel

class MediaPlayerRepositoryImpl(
    private val mediaPlayer : MediaPlayer,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
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

    override fun getTrack() : TrackModel {
        return clickedTracks[0]
    }

    override fun isNightTheme() : Boolean {
        return App.darkTheme
    }

    override suspend fun insertDbTrackToFavorite(track: TrackModel) {
        val listTracks = arrayListOf<TrackModel>()
        listTracks.add(track)
        val trackEntity = convertToTrackEntity(listTracks)
        appDatabase.trackDao().insertFavoriteTrack(trackEntity)
        clickedTracks[0].isFavorite = true
    }

    private fun convertToTrackEntity(listTracks: ArrayList<TrackModel>): ArrayList<TrackEntity> {
        return listTracks.map { track -> trackDbConvertor.map(track) }  as ArrayList<TrackEntity>
    }

    override suspend fun deleteDbTrackFromFavorite(trackId: String) {
        appDatabase.trackDao().deleteTrack(trackId)
        clickedTracks[0].isFavorite = false
    }
}