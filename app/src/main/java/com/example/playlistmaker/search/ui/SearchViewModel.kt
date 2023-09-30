package com.example.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.TrackSearchModel
import com.example.playlistmaker.sharing.domain.App


class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val app: App
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    lateinit var searchRunnable: Runnable
    private val _stateLiveData = MutableLiveData<SearchState>()
    fun stateLiveData(): LiveData<SearchState> = _stateLiveData

    private var latestSearchText: String? = null

    fun searchDebounce(changedText: String, hasError: Boolean) {
        if (latestSearchText == changedText && !hasError) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages("")
        searchRunnable = Runnable { searchSong(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            postTime
        )
    }

    private fun searchSong(expression: String) {
        if (expression.isNotEmpty()) {
            updateState(SearchState.Loading)

            searchInteractor.searchTracks(expression, object : SearchInteractor.SearchConsumer {
                override fun consume(searchTracks: List<TrackSearchModel>?, hasError: Boolean?) {
                    val tracks = arrayListOf<TrackSearchModel>()

                    if (searchTracks != null) {
                        tracks.addAll(searchTracks)
                        App.playedTracks.addAll(searchTracks)
                        App.historyTracks.add(0,tracks[0])

                        when {
                            tracks.isEmpty() -> {
                                updateState(SearchState.Empty())
                            }

                            tracks.isNotEmpty() -> {
                                updateState(SearchState.Content(tracks))
                            }
                        }
                    } else {
                        updateState(SearchState.Error())
                    }
                }
            })
        }
    }

    fun getTracksHistory() {
        searchInteractor.getTracksHistory(object : SearchInteractor.HistoryConsumer {
            override fun consume(tracks: List<TrackSearchModel>?) {
                if (tracks.isNullOrEmpty()) {
                    updateState(SearchState.EmptyHistoryList())
                } else {
                    updateState(SearchState.ContentHistoryList(tracks))
                }
            }
        })
    }

    fun addTrackToHistory(track: TrackSearchModel) {
        searchInteractor.addTrackToHistory(track)

    }

    fun clearHistory() {
        searchInteractor.clearHistory()
    }

    private fun updateState(state: SearchState) {
        _stateLiveData.postValue(state)
    }

    override fun onCleared() {
        handler.removeCallbacks(searchRunnable)
    }

    companion object {

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as App)
                SearchViewModel(
                    searchInteractor = Creator.provideSearchInteractor(app.applicationContext),
                    app = app
                )
            }
        }

        private const val SEARCH_DEBOUNCE_DELAY = 2000L
            }
}