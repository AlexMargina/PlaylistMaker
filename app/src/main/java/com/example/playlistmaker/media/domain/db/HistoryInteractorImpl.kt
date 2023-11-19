package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImpl (private val historyRepository: HistoryRepository): HistoryInteractor {
    override fun historyTracks(): Flow<List<TrackModel>> {

        return historyRepository.historyTracks()
    }
}