package com.example.playlistmaker.sharing.domain

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.player.data.TracksRepositoryImpl
import com.example.playlistmaker.player.data.network.RetrofitNetworkClient
import com.example.playlistmaker.player.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.TracksInteractorImpl

const val MUSIC_MAKER_PREFERENCES = "music_maker_preferences"
const val DARK_THEME_ENABLED = "dark_theme_enabled"
const val CLICKED_SEARCH_TRACK = "clicked_search_track"

class App : Application() {

    companion object {
        var activeTracks = mutableListOf<Track>()
        var darkTheme = false
    }


    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences(MUSIC_MAKER_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPreferences.getString (DARK_THEME_ENABLED, "false").toBoolean()
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        ) }
    fun getRepository(): TracksRepositoryImpl {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getRepository())
    }
}

