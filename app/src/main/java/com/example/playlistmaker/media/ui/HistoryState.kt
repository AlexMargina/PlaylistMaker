package com.example.playlistmaker.media.ui


import com.example.playlistmaker.search.domain.TrackModel

sealed class HistoryState {

    object Loading : HistoryState()

    data class Content(
        val movies: List<TrackModel>
    ) : HistoryState()

    data class Empty(
        val message: String
    ) : HistoryState()
}