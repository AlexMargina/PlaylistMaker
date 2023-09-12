package com.example.playlistmaker.setting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.sharing.domain.Track

class SettingViewModel : ViewModel() {

    private val _state = MutableLiveData<Track>()
    val state :LiveData<Track> = _state


    val getProvideTrack = Creator.getTracksRepository()

    fun LoadData () {
        _state.value
    }

}