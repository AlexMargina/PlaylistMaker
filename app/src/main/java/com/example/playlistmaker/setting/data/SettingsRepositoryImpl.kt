package com.example.playlistmaker.setting.data

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.setting.domain.SettingsRepository

class SettingsRepositoryImpl() : SettingsRepository {

    override fun switchTheme(darkThemeEnabled: Boolean, ) {
        //запись в файл настроек
        AppPreferences.darkTheme = darkThemeEnabled
        // применение темы
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
    override fun getThemeSettings(): Boolean {

        return when (AppPreferences.darkTheme==null) {
            true -> false
            false -> AppPreferences.darkTheme !!
        }
    }


}