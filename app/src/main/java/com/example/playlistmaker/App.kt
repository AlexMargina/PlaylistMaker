package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

const val MUSIC_MAKER_PREFERENCES = "music_maker_preferences"
const val DARK_THEME_ENABLED = "dark_theme_enabled"
const val CLICKED_SEARCH_TRACK = "clicked_search_track"

class App : Application() {

    var darkTheme = false
    var clickedSearchSongs = arrayListOf<Track>() // просмотренные песни
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)
        darkTheme = sharedPreferences.getString (DARK_THEME_ENABLED, "false").toBoolean()
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

