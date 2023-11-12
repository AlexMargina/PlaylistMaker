package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow


interface SearchInteractor {
    suspend fun searchTracks (expression: String): Flow<Pair<List<TrackModel>?, Boolean?>>
    fun getTracksHistory(consumer: HistoryConsumer)
    fun addTrackToHistory(track: TrackModel)
    fun clearHistory()

    interface HistoryConsumer {
        fun consume(tracks: List<TrackModel>?)
    }
}