package com.example.playlistmaker

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

const val MUSIC_MAKER_PREFERENCES = "music_maker_preferences"
const val DARK_TEME_ENABLED = "dark_teme_enabled"

@Suppress("NAME_SHADOWING")
class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)

        darkTheme = sharedPrefsApp.getString (DARK_TEME_ENABLED, "false").toBoolean()
        Log.w("maalmi_App", "Прочитано с файла ${darkTheme}")
        switchTheme(darkTheme)
    }
    fun switchTheme(darkThemeEnabled: Boolean) {
        //darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        ) }
}

