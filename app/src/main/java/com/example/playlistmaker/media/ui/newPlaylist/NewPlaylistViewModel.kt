package com.example.playlistmaker.media.ui.newPlaylist

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.media.domain.newPlaylist.NewPlaylistInteractor
import com.example.playlistmaker.media.domain.playlist.PlaylistInteractor
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val interactor: PlaylistInteractor,
    private val newPlaylistInteractor: NewPlaylistInteractor,
) : ViewModel() {

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private var selectedUri: Uri? = null

    private var _playlistLiveData = MutableLiveData<List<Playlist>>()
    val playlistLiveData: LiveData<List<Playlist>> = _playlistLiveData

    private var _pictureLiveData = MutableLiveData<Uri?>()
    val pictureLiveData: LiveData<Uri?> = _pictureLiveData


    fun loadPickMedia(newPlaylistFragment: NewPlaylistFragment) {
        pickMedia =
            newPlaylistFragment.registerForActivityResult(ActivityResultContracts
                .PickVisualMedia()) { uri ->
                viewModelScope.launch {
                    if (uri != null) {  selectedUri = uri }
                    _pictureLiveData.postValue(uri)
                }
            }
    }

    fun insertPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            if (selectedUri != null) {
                newPlaylistInteractor.savePicture(selectedUri !!, playlist.namePl)
            }
            interactor.insertPlaylist(playlist)
            interactor.getPlaylists().collect { list ->
                _playlistLiveData.postValue(list)
            }
        }
    }



    fun loadCover() {
        viewModelScope.launch {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }
}

