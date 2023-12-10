package com.example.playlistmaker.media.favorite.domain

import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    fun favoriteTracks(): Flow<ArrayList<TrackModel>>
    fun setClickedTrack(track: TrackModel)
}