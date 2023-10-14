package com.example.playlistmaker.setting.data

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.DARK_THEME_ENABLED
import com.example.playlistmaker.setting.domain.SettingsRepository

class SettingsRepositoryImpl(private val prefs: SharedPreferences, private val applicationContext: Context) : SettingsRepository {

    override fun switchTheme(darkThemeEnabled: Boolean ) {
        //запись в файл настроек
        prefs.edit().putBoolean(DARK_THEME_ENABLED, false).apply( )
    }
    override fun getThemeSettings(): Boolean {
        return prefs.getBoolean(DARK_THEME_ENABLED, false)
    }
}