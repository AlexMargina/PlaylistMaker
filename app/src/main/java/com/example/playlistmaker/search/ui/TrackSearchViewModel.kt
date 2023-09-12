package com.example.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.player.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TrackSearchScreenState
import com.example.playlistmaker.sharing.domain.App
import com.example.playlistmaker.sharing.domain.Track

class TrackSearchViewModel (private val trackId: String,
                            private val tracksInteractor: TracksInteractor, ) : ViewModel()
    {
        private var screenStateLiveData = MutableLiveData<TrackSearchScreenState>(TrackSearchScreenState.Loading)

        init {
            tracksInteractor.loadTrackData(
                trackId = trackId
            ) { trackModel ->
                screenStateLiveData.postValue(
                    TrackSearchScreenState.Content(trackModel as Track)
                )
            }
        }

        fun getScreenStateLiveData(): LiveData<TrackSearchScreenState> = screenStateLiveData

        companion object {
            fun getViewModelFactory(trackId: String): ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val interactor = (this[APPLICATION_KEY] as App).provideTracksInteractor()

                    TrackSearchViewModel(
                        trackId,
                        interactor,
                    )
                }
            }
        }

    }