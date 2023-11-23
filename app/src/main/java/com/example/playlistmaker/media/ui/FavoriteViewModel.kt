package com.example.playlistmaker.media.ui


import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.db.FavoriteInteractor
import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.launch


class FavoriteViewModel(private val context: Context, private val favoriteInteractor: FavoriteInteractor ) : ViewModel() {

    private val _stateLiveData = MutableLiveData<FavoriteState>()
    val stateLiveData: LiveData<FavoriteState> =_stateLiveData
    fun observeState(): LiveData<FavoriteState> = stateLiveData

    init {
        fillData()
        Log.d("MAALMI", "INIT in FavoriteViewModel")
    }

    fun fillData() {
        renderState(FavoriteState.Loading)
        viewModelScope.launch {
            favoriteInteractor
                .favoriteTracks()
                .collect { tracks ->
                    processResult(tracks)
                }

        }
    }

    private fun processResult(tracks: ArrayList<TrackModel>) {
        if (tracks.isEmpty()) {
            renderState(FavoriteState.Empty(context.getString(R.string.empty_favorites)))
        } else {
            renderState(FavoriteState.Content(tracks))
            Log.d("MAALMI", "processResult = ${tracks.size}")
        }
    }

    private fun renderState(state: FavoriteState) {
        _stateLiveData.postValue(state)
    }

    fun setClickedTrack(track: TrackModel, favoriteFragment: FavoriteFragment) {
        favoriteInteractor.setClickedTrack(track)
    }
}
