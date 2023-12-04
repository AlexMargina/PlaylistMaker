package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.TrackModel

interface SearchDataStorage {
    suspend fun getSearchHistory(): ArrayList<TrackDto>
    suspend fun clearHistory()
    suspend fun addTClickedSearchSongs(track: TrackDto)

}