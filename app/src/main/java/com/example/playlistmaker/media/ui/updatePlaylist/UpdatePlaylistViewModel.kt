package com.example.playlistmaker.media.ui.updatePlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.media.domain.newPlaylist.NewPlaylistInteractor
import com.example.playlistmaker.media.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.media.ui.displayPlaylist.DisplayPlaylistFragment
import com.example.playlistmaker.media.ui.newPlaylist.NewPlaylistViewModel
import kotlinx.coroutines.launch

class UpdatePlaylistViewModel(private val interactor: PlaylistInteractor,
                              private val newPlaylistInteractor: NewPlaylistInteractor)
    : NewPlaylistViewModel(interactor, newPlaylistInteractor)
{
    private var _updateLiveData = MutableLiveData<Playlist>()
    val updateLiveData: LiveData<Playlist> = _updateLiveData
    private val _update = MutableLiveData<Boolean>()
    val update: LiveData<Boolean> = _update

    fun updatePlaylist(
        idPl: Int?,
        imagePl: String?,
        namePl: String?,
        descriptPl: String?,
    ) {
        viewModelScope.launch {
            interactor. updatePlaylist(idPl, namePl, imagePl, descriptPl)
                }
        }


    fun initialization () {
        // вариант вариант передачи данных плэйлиста из DisplayPlaylistFragment
        val actualPlaylist = DisplayPlaylistFragment.actualPlaylist
        _updateLiveData.postValue(actualPlaylist!!)
    }
}