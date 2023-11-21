package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl (private val favoriteRepository: FavoriteRepository): FavoriteInteractor {
    override fun favoriteTracks(): Flow<List<TrackModel>> {

        return favoriteRepository.favoriteTracks()
    }
}