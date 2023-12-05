package com.example.playlistmaker.player.domain

import com.example.playlistmaker.media.favorite.domain.FavoriteRepository
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.TrackModel

class MediaPlayerInteractorImpl(
        val mediaPlayerRepository: MediaPlayerRepository,
        val searchRepository: SearchRepository,
        val favoriteRepository: FavoriteRepository
    ) :  MediaPlayerInteractor {

    override val isPlaying = mediaPlayerRepository.isPlaying

    override fun preparePlayer(url: String, onPreparedListener: () -> Unit) {
        mediaPlayerRepository.preparePlayer(url, onPreparedListener)
    }

    override fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        mediaPlayerRepository.setOnCompletionListener(onCompletionListener)
    }

    override fun currentPosition(): Int {
        return mediaPlayerRepository.currentPosition()
    }

    override fun startPlayer() {
        mediaPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        mediaPlayerRepository.pausePlayer()
    }

    override fun stopPlayer() {
        mediaPlayerRepository.stopPlayer()
    }

    override fun destroyPlayer() {
        mediaPlayerRepository.destroyPlayer()
    }

    override fun getTrack(): TrackModel {
        return mediaPlayerRepository.getTrack()
    }

    override suspend fun saveTrack(track: TrackModel) {
        searchRepository.addTrackToHistory(track)
    }

    override fun isNightTheme(): Boolean {
        return mediaPlayerRepository.isNightTheme()
    }

    override suspend fun insertTrackToFavorite(track: TrackModel) {
        favoriteRepository.insertDbTrackToFavorite(track)
    }

    override suspend fun deleteTrackFromFavorite(trackId: String) {
        favoriteRepository.deleteDbTrackFromFavorite(trackId)
    }

}



