package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackDto

interface SearchDataStorage {
    fun getSearchHistory(): ArrayList<TrackDto>
    fun clearHistory()
    fun addTClickedSearchSongs(track: TrackDto)
}