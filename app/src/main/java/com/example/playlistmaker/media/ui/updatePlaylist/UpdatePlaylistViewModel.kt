package com.example.playlistmaker.media.ui.updatePlaylist

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.media.domain.newPlaylist.NewPlaylistInteractor
import com.example.playlistmaker.media.domain.playlist.PlaylistInteractor
import com.example.playlistmaker.media.ui.displayPlaylist.DisplayPlaylistFragment
import com.example.playlistmaker.media.ui.newPlaylist.NewPlaylistViewModel
import kotlinx.coroutines.launch

class UpdatePlaylistViewModel(
      interactor: PlaylistInteractor,
      newPlaylistInteractor: NewPlaylistInteractor
) : NewPlaylistViewModel(interactor, newPlaylistInteractor) {
    private var _updateLiveData = MutableLiveData<Playlist>()
    val updateLiveData: LiveData<Playlist> = _updateLiveData
    private val _update = MutableLiveData<Boolean>()
    val update: LiveData<Boolean> = _update


    fun updatePl(idPl: Int?, imagePl: String?, namePl: String?, descriptPl: String?) {
        viewModelScope.launch {
            interactor.updatePl(idPl, namePl, imagePl, descriptPl)
        }
    }


    fun deletePicture(oldNamePl: String) {
        Log.d("MAALMI_NewPlaylistVM", "Готовим на удаление  ${oldNamePl}")
        viewModelScope.launch {
                newPlaylistInteractor.deletePicture(oldNamePl)
                Log.d("MAALMI_NewPlaylistVM", "Удалили = ${oldNamePl}")
        }
    }

    override fun savePicture(uri: Uri?, namePl: String) {
        Log.d("MAALMI_NewPlaylistVM", "Готовим на сохранение = ${uri?.encodedPath}")
        viewModelScope.launch {
            if (uri != null) {
                newPlaylistInteractor.savePicture(uri, namePl)
                Log.d("MAALMI_NewPlaylistVM", "Отправляем на сохранение = ${uri.encodedPath}")
            }
        }
    }

    fun initialization() {
        // Второй вариант передачи данных плэйлиста из DisplayPlaylistFragment
        val actualPlaylist = DisplayPlaylistFragment.actualPlaylist
        _updateLiveData.postValue(actualPlaylist !!)
    }
}