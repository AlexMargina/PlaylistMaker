package com.example.playlistmaker.setting.data

import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.setting.domain.SettingsRepository

class SettingsInteractorImpl (private val repository: SettingsRepository) : SettingsInteractor {

    override fun getThemeSettings(): Boolean {
         return repository.getThemeSettings() !!
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        repository.switchTheme(darkThemeEnabled)
    }
}