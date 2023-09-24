package com.example.playlistmaker.search.domain

import com.example.playlistmaker.sharing.domain.Track



class SearchInteractorImpl(
    private val historySearchDataStore: HistorySearchDataStore,
    private val repository: SearchRepository,
) : SearchInteractor {
    override fun clearHistory() {
        historySearchDataStore.clearHistory()
    }

    override fun getHistory(): List<Track> {
        return historySearchDataStore.getHistory()
    }

    override fun loadTracks(query: String) {
        repository.loadTracks(query)
    }

    override fun writeHistory(historyTracks: List<Track>) {
        historySearchDataStore.writeHistory(historyTracks)
    }

    override fun onTracksLoader(listener: ResultLoad) {
        repository.tracksLoadResultListener = listener
    }

    override fun onDestroyView() {
        repository.tracksLoadResultListener = null
    }
}


