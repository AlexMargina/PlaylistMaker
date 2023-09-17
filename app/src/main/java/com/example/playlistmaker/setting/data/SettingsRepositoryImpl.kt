package com.example.playlistmaker.setting.data

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.setting.domain.SettingsRepository

class SettingsRepositoryImpl() : SettingsRepository {

    override fun getThemeSettings(): Boolean {

        return when (AppPreferences.darkTheme==null) {
            true -> false
            false -> AppPreferences.darkTheme !!
        }
    }

    override fun switchTheme(darkThemeEnabled: Boolean, ) {

        AppPreferences.darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
                Log.d ("MAALMI", "Установлена темная тема")
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
                Log.d ("MAALMI", "Установлена светлая тема")
            }
        )
    }

}