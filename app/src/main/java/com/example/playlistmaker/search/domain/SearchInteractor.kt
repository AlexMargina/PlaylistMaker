package com.example.playlistmaker.search.domain

import com.example.playlistmaker.sharing.domain.Track


interface SearchInteractor {
    fun clearHistory()
    fun getHistory(): List<Track>
    fun loadTracks(query: String)
    fun writeHistory(historyTracks: List<Track>)
    fun onDestroyView()
    fun onTracksLoader(listener: ResultLoad)


    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }
}