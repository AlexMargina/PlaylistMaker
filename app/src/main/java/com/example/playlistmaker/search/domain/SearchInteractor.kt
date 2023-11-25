package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow


interface SearchInteractor {
    suspend fun searchTracks (expression: String): Flow<Pair<List<TrackModel>?, Boolean?>>
    suspend fun getTracksHistory(consumer: HistoryConsumer)
    suspend fun addTrackToHistory(track: TrackModel)
    suspend fun clearHistory()

    interface HistoryConsumer {
        fun consume(tracks: List<TrackModel>?)
    }
}