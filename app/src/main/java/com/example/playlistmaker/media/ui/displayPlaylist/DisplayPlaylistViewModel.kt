package com.example.playlistmaker.media.ui.displayPlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.media.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.media.ui.playlist.PlaylistState

class DisplayPlaylistViewModel(private val interactor : PlaylistInteractor) : ViewModel(){

    private var _liveData = MutableLiveData<PlaylistState>()
    val liveData: LiveData<PlaylistState> = _liveData

}