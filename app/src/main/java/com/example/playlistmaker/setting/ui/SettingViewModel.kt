package com.example.playlistmaker.setting.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.App
import com.example.playlistmaker.App.Companion.extraMail
import com.example.playlistmaker.App.Companion.extraSubject
import com.example.playlistmaker.App.Companion.extraText
import com.example.playlistmaker.App.Companion.oferUrl
import com.example.playlistmaker.App.Companion.sendTitle
import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {
    private val _theme = MutableLiveData<Boolean>()
    val theme: LiveData<Boolean> = _theme

    init {
        _theme.postValue(settingsInteractor.getThemeSettings())
    }

    fun switchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
        _theme.postValue(checked)
    }

    fun getThemeState(): Boolean {
        return settingsInteractor.getThemeSettings()
    }

    fun shareApp() {
        sharingInteractor.shareApp(App.sendText, sendTitle)
    }

    fun writeInSupport() {
        sharingInteractor.openSupport(extraText, extraMail, extraSubject)
    }

    fun openUserAgreement() {
        sharingInteractor.openTerms(oferUrl)
    }

}