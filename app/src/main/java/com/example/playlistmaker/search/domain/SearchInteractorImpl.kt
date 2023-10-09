package com.example.playlistmaker.search.domain

import android.util.Log
import com.example.playlistmaker.search.data.dto.ResponseStatus
import java.util.concurrent.Executors


class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: SearchInteractor.SearchConsumer) {
        executor.execute {
            Log.d ("MAALMI_SearchInteractor", "Пришло на оправку searchTracks ($expression)")
            val resource = repository.searchTrack(expression)
            Log.d ("MAALMI_SearchInteractor", "Вернулось с Repository searchTracks (${resource.data.toString()})")
            when(resource) {
                is ResponseStatus.Success -> { consumer.consume(resource.data, false) }
                is ResponseStatus.Error -> { consumer.consume(null,  true) }
            }
        }
    }

    override fun getTracksHistory(consumer: SearchInteractor.HistoryConsumer) {
        Log.d ("MAALMI_SearchInteractor", "Пришло в getTracksHistory (${repository.getTrackHistoryList().isNullOrEmpty()})")
        consumer.consume(repository.getTrackHistoryList())
    }

    override fun addTrackToHistory(track: TrackModel) {
        repository.addTrackInHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}


