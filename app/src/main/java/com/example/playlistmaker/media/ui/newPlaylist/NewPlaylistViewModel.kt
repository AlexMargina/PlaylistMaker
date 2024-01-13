package com.example.playlistmaker.media.ui.newPlaylist

import android.net.Uri
import android.util.Log
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

open class NewPlaylistViewModel(
     val interactor: PlaylistInteractor,
     val newPlaylistInteractor: NewPlaylistInteractor,
) : ViewModel() {

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
     var selectedUri: Uri? = null

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

    open fun savePicture(uri: Uri?, namePl: String) {
        viewModelScope.launch {
            if (uri != null) {
                newPlaylistInteractor.savePicture(uri, namePl)
                Log.d("MAALMI_NewPlaylistVM", "Отправляем на сохранение = ${uri.encodedPath}")
            }
        }
    }

    fun insertPlaylist(playlist: Playlist) {
        viewModelScope.launch {
            savePicture(selectedUri, playlist.namePl)

            interactor.insertPlaylist(playlist)
            interactor.getPlaylists().collect { list ->
                _playlistLiveData.postValue(list)
            }
        }
    }

    fun imagePath () : String {
        return newPlaylistInteractor.imagePath()
    }

    fun loadCover() {
        viewModelScope.launch {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }
}

