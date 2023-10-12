package com.example.playlistmaker.setting.data

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.setting.domain.SettingsRepository
import com.example.playlistmaker.sharing.data.AppPreferences

class SettingsRepositoryImpl(private val applicationContext: Context) : SettingsRepository {

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