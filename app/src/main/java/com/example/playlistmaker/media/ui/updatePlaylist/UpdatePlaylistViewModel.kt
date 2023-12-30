package com.example.playlistmaker.media.ui.updatePlaylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.media.domain.newPlaylist.NewPlaylistInteractor
import com.example.playlistmaker.media.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.media.ui.newPlaylist.NewPlaylistViewModel

class UpdatePlaylistViewModel(private val interactor: PlaylistInteractor,
                              private val newPlaylistInteractor: NewPlaylistInteractor)
    : NewPlaylistViewModel(interactor, newPlaylistInteractor)
{
    private val _update = MutableLiveData<Boolean>()
    val update: LiveData<Boolean> = _update

    fun updatePlaylist(
        idPl: Int,
        imagePl: String,
        namePl: String,
        descriptPl: String,
    ) {
//        viewModelScope.launch {
//            interactor.updatePlaylistInfo(playlistId, loadUri, title, description)
//                .collect { result ->
//                    _update.postValue(result)
//                }
//        }
    }
}