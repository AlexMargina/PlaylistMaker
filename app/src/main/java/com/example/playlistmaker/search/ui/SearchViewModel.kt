package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchState
import com.example.playlistmaker.search.domain.TrackModel
import com.example.playlistmaker.utils.debounce


class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    private val _stateLiveData = MutableLiveData<SearchState>()
    fun stateLiveData(): LiveData<SearchState> = _stateLiveData

    private var latestSearchText: String? = null

    private val trackSearchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
        searchSong(changedText)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    private fun searchSong(changedText: String) {
        if (changedText.isNotEmpty()) {
            updateState(SearchState.Loading)

            searchInteractor.searchTracks(changedText, object : SearchInteractor.SearchConsumer {
                override fun consume(searchTracks: List<TrackModel>?, hasError: Boolean?) {
                    val tracks = arrayListOf<TrackModel>()

                    if (searchTracks != null) {
                        tracks.addAll(searchTracks)
                        playedTracks.addAll(searchTracks)
                    }

                    when {
                        tracks.isEmpty() -> {
                            updateState(SearchState.Empty())
                        }

                        tracks.isNotEmpty() -> {
                            updateState(SearchState.Content(tracks))
                        }
                    }
                }
            })
        }
    }

    fun getTracksHistory() {
        searchInteractor.getTracksHistory(object : SearchInteractor.HistoryConsumer {
            override fun consume(tracks: List<TrackModel>?) {
                if (tracks.isNullOrEmpty()) {
                    updateState(SearchState.EmptyHistoryList())
                } else {
                    updateState(SearchState.ContentHistoryList(tracks))
                }
            }
        })
    }

    fun addTrackToHistory(track: TrackModel, activity: SearchFragment) {
        searchInteractor.addTrackToHistory(track)
    }

    fun clearHistory() {
        searchInteractor.clearHistory()
    }

    private fun updateState(state: SearchState) {
        _stateLiveData.postValue(state)
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2200L
        var playedTracks = arrayListOf<TrackModel>()
    }
}