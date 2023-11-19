package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface HistoryInteractor {
    fun historyTracks(): Flow<List<TrackModel>>
}