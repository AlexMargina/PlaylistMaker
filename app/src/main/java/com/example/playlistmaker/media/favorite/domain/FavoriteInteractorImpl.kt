package com.example.playlistmaker.media.favorite.domain

import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl (private val favoriteRepository: FavoriteRepository):
    FavoriteInteractor {
    override fun favoriteTracks(): Flow<ArrayList<TrackModel>> {

        return favoriteRepository.favoriteTracks()
    }

    override fun setClickedTrack(track: TrackModel) {
        favoriteRepository.setClickedTrack(track)
    }
}