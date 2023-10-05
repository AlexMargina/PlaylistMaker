package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.data.dto.ResponseStatus

interface SearchRepository {
    fun searchTrack(expression: String): ResponseStatus<List<TrackModel>>
    fun getTrackHistoryList(): List<TrackModel>
    fun addTrackInHistory(track: TrackModel)
    fun clearHistory()
}