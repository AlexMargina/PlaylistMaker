package com.example.playlistmaker.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.domain.ResultLoad
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.TrackSearchScreenState
import com.example.playlistmaker.sharing.domain.App
import com.example.playlistmaker.sharing.domain.Track

class SearchViewModel (private val trackId: String,
                       private val interactor: SearchInteractor, ) : ViewModel()
    {
        private var screenStateLiveData = MutableLiveData<TrackSearchScreenState>(TrackSearchScreenState.Loading)
        private val searchedSong = mutableListOf<Track>() // песни найденные через iTunesApi
        private var historySongs = mutableListOf<Track>() // песни сохраненные по клику

        private val _state = MutableLiveData<SearchState>()
        val state: LiveData<SearchState> = _state



        init {
            interactor.onTracksLoader(object : ResultLoad {
                override fun onSuccess(tracks: List<Track>) {
                    Log.d ("MAALMI", "init ViewModel ${state.toString()}")
                    if (tracks.isEmpty()) {
                        _state.postValue(SearchState.Empty)
                    } else {
                        _state.postValue(SearchState.Tracks(tracks))
                    }
                }

                override fun onError() {
                    _state.postValue(SearchState.Error)
                }
            })

            historySongs.addAll(interactor.getHistory())
        }






        fun getScreenStateLiveData(): LiveData<TrackSearchScreenState> = screenStateLiveData

        fun getLoadingLiveData(){

        }




        companion object {
            fun getViewModelFactory(trackId: String): ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val interactor = (this[APPLICATION_KEY] as App).provideSearchInteractor()

                    SearchViewModel(
                        trackId,
                        interactor,
                    )
                }
            }
        }

        fun searchGetFocus(hasFocus: Boolean, text: String) {
            if (hasFocus && text.isEmpty() && (interactor.getHistory()).isNotEmpty()) {
                _state.postValue(SearchState.History(interactor.getHistory()))
            }
        }


        fun loadTracks(query: String) {
            if (query.isEmpty()) {
                return
            }
            _state.postValue(SearchState.Loading)
            interactor.loadTracks(
                query = query
            )
        }

        fun openHistoryTrack(track: Track) {
            historySongs.remove(track)
            historySongs.add(0, track)
            interactor.writeHistory(historySongs)
            _state.postValue(SearchState.History(historySongs))
        }

        fun onDestroyView() {
            interactor.onDestroyView()
        }

        fun clearHistory() {
            interactor.clearHistory()
            _state.postValue(SearchState.History(interactor.getHistory()))
        }

        fun clearSearchText() {
            _state.postValue(SearchState.History(interactor.getHistory(), true))
        }

        fun openTrack(track: Track) {
            if (historySongs.contains(track)) {
                historySongs.remove(track)
                historySongs.add(0, track)
            } else {
                historySongs.add(0, track)
            }
            if (historySongs.size == 11) {
                historySongs.removeAt(10)
            }
            interactor.writeHistory(historySongs)
        }




    }