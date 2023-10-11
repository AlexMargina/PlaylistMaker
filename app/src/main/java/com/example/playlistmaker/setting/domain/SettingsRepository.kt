package com.example.playlistmaker.setting.domain

interface SettingsRepository  {

    fun getThemeSettings(): Boolean?

    fun switchTheme(darkThemeEnabled: Boolean)

}