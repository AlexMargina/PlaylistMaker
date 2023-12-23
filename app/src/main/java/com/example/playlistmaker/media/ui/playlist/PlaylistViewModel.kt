package com.example.playlistmaker.media.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.media.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistViewModel (private val interactor : PlaylistInteractor) : ViewModel() {

    private var _liveData = MutableLiveData<PlaylistState>()
    val liveData: LiveData<PlaylistState> = _liveData

    fun showPlaylist() {
        viewModelScope.launch {
            interactor.getPlaylists()
                .collect { processResult(it) }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _liveData.postValue(PlaylistState.Empty)
        } else {
            _liveData.postValue(PlaylistState.Playlists(playlists))
        }
    }
}
