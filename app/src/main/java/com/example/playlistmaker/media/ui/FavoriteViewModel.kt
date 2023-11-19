package com.example.playlistmaker.media.ui


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.db.HistoryInteractor
import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.launch


class FavoriteViewModel(private val context: Context, private val historyInteractor: HistoryInteractor ) : ViewModel() {

    private var _liveData = MutableLiveData<String>()
    val liveData: LiveData<String> = _liveData


    private val stateLiveData = MutableLiveData<HistoryState>()

    fun observeState(): LiveData<HistoryState> = stateLiveData

    fun fillData() {
        renderState(HistoryState.Loading)
        viewModelScope.launch {
            historyInteractor
                .historyTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<TrackModel>) {
        if (tracks.isEmpty()) {
            renderState(HistoryState.Empty(context.getString(R.string.empty_favorites)))
        } else {
            renderState(HistoryState.Content(tracks))
        }
    }

    private fun renderState(state: HistoryState) {
        stateLiveData.postValue(state)
    }
}
