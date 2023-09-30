package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.TrackSearchModel

sealed interface SearchState {

    object Loading : SearchState

    data class Content(val tracks: List<TrackSearchModel>) : SearchState

    class Error : SearchState

    class Empty : SearchState

    data class ContentHistoryList(val historyList: List<TrackSearchModel>) : SearchState

    class EmptyHistoryList() : SearchState

}