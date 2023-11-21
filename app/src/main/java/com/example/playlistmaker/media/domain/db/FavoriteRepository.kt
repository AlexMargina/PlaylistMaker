package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun favoriteTracks(): Flow<List<TrackModel>>

    suspend fun deleteDbTrack (trackId :String)

    suspend fun insertDbTrack (track: TrackModel)
}