package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.dto.ResponseStatus
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchTrack(expression: String): Flow<ResponseStatus<List<TrackModel>>>
    fun getTrackHistoryList(): List<TrackModel>
    fun addTrackInHistory(track: TrackModel)
    fun clearHistory()


}