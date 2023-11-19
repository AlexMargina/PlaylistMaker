package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun historyTracks(): Flow<List<TrackModel>>
}