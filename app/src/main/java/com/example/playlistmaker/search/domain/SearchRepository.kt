package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.dto.ResponseStatus
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchTrack(expression: String): Flow<ResponseStatus<List<TrackModel>>>
    suspend fun getTrackHistoryList(): List<TrackModel>
    suspend fun addTrackInHistory(track: TrackModel)
    suspend fun clearHistory()


}