package com.example.playlistmaker.setting.data

import android.content.SharedPreferences
import com.example.playlistmaker.App
import com.example.playlistmaker.DARK_THEME_ENABLED
import com.example.playlistmaker.setting.domain.SettingsRepository

class SettingsRepositoryImpl(private val prefs: SharedPreferences) : SettingsRepository {

    override fun switchTheme(darkThemeEnabled: Boolean ) {
        //запись в файл настроек
        prefs.edit().putBoolean(DARK_THEME_ENABLED, darkThemeEnabled).apply( )
        App().switchTheme(darkThemeEnabled)
    }

    override fun getThemeSettings(): Boolean {
        return prefs.getBoolean(DARK_THEME_ENABLED, false)
    }


}



