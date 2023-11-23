package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun favoriteTracks(): Flow<ArrayList<TrackModel>>

    suspend fun deleteDbTrack (trackId :String)


    fun setClickedTrack(track: TrackModel)
}