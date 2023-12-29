package com.example.playlistmaker.media.ui.displayPlaylist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.media.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.media.ui.playlist.PlaylistState
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.launch

class DisplayPlaylistViewModel(private val interactor : PlaylistInteractor,private val searchInteractor : SearchInteractor) : ViewModel(){

    private var _playlistLiveData = MutableLiveData<Playlist>()
    val playlistLiveData: LiveData<Playlist> = _playlistLiveData
    private var _liveData = MutableLiveData<PlaylistState>()
    val liveData: LiveData<PlaylistState> = _liveData

    // Получить нужный плэйлист и обработать его
    fun getPlaylistById(idPl: Int) {
        viewModelScope.launch {
            val playlist = interactor.getPlaylistById(idPl)
            _playlistLiveData.postValue(playlist)
        }
    }

    fun getPlaylist() {
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

    fun addTrackToHistory(track:TrackModel) {
        Log.d("MAALMI_SearchVM", "2. addTrackToHistory")
        viewModelScope.launch { searchInteractor.addTrackToHistory(track) }
    }
}