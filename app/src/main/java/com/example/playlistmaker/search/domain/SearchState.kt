package com.example.playlistmaker.search.domain

sealed interface SearchState {

    object Loading : SearchState

    data class Content(val tracks: List<TrackModel>) : SearchState

    data class ContentHistory(val tracks: List<TrackModel>) : SearchState

    class Error : SearchState

    class Empty : SearchState

    class EmptyHistoryList() : SearchState

}