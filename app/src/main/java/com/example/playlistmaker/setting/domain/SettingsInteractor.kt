package com.example.playlistmaker.setting.domain

interface SettingsInteractor {


    fun getThemeSettings(): Boolean

    fun switchTheme(darkThemeEnabled: Boolean)
}