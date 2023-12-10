package com.example.playlistmaker.setting.data

import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
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
        val defaultSystemDarkMode = isDarkModeOn()
        return prefs.getBoolean(DARK_THEME_ENABLED, defaultSystemDarkMode)
    }

        fun isDarkModeOn(): Boolean {
            val nightModeFlags = Resources.getSystem().configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val isDarkModeOn = nightModeFlags == Configuration.UI_MODE_NIGHT_YES
            return isDarkModeOn
        }
}