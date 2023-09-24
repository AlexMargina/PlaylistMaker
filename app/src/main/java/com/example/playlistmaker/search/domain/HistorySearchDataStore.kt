package com.example.playlistmaker.search.domain

import com.example.playlistmaker.sharing.domain.Track

interface HistorySearchDataStore {
    fun clearHistory()

    fun getHistory(): List<Track>

    fun writeHistory(tracks: List<Track>)
}