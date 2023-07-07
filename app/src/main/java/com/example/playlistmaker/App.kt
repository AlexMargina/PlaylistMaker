package com.example.playlistmaker

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.GsonBuilder

const val MUSIC_MAKER_PREFERENCES = "music_maker_preferences"
const val DARK_THEME_ENABLED = "dark_theme_enabled"
const val CLICKED_SEARCH_TRACK = "clicked_search_track"

open class App : Application() {

    companion object {
        fun writeClickedSearchSongs(clickedSearchSongs: ArrayList<Track>) {

        }

        var clickedSearchSongs = arrayListOf<Track>() // просмотренные песни
    }

    var darkTheme = false


    override fun onCreate() {
        super.onCreate()
        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)

        darkTheme = sharedPrefsApp.getString (DARK_THEME_ENABLED, "false").toBoolean()
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

    fun writeClickedSearchSongs(clickedSearchSongs: ArrayList<Track>) {

        val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)

        val json = GsonBuilder().create()
        val jsonString = json.toJson(clickedSearchSongs)
        sharedPrefsApp.edit()
            .putString(CLICKED_SEARCH_TRACK, jsonString)
            .apply()
        Log.d(LOG_TAG, "Перед записью в файл: ${clickedSearchSongs.toString()}")
        Log.d(LOG_TAG, "Записано в файл $jsonString")

    }
}

