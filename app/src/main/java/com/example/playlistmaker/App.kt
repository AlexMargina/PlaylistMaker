package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.sharing.data.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


const val MUSIC_MAKER_PREFERENCES = "music_maker_preferences"
const val CLICKED_SEARCH_TRACK = "clicked_search_track"
const val DARK_THEME_ENABLED = "DARK_THEME_ENABLED"


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App as Application)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        AppPreferences.setup(applicationContext)
        if (AppPreferences.darkTheme != null) {
            darkTheme = AppPreferences.darkTheme !!
            switchTheme(darkTheme)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        var darkTheme = false
    }
}