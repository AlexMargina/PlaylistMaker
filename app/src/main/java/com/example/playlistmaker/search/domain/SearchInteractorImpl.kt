package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.dto.ResponseStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    override suspend fun searchTracks(expression: String): Flow<Pair<List<TrackModel>?, Boolean?>> {

        return repository.searchTrack(expression).map { result ->
            when (result) {
                is ResponseStatus.Success -> {
                    Pair(result.data, null)
                }

                is ResponseStatus.Error<*> -> {
                    Pair(null, result.hasError)
                }
            }
        }
    }

    override fun getTracksHistory(consumer: SearchInteractor.HistoryConsumer) {
        consumer.consume(repository.getTrackHistoryList())
    }

    override fun addTrackToHistory(track: TrackModel) {
        repository.addTrackInHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}


