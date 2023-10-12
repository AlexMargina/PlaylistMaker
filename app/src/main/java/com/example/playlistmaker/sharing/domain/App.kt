package com.example.playlistmaker.sharing.domain

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule
import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.search.domain.TrackModel
import com.example.playlistmaker.sharing.data.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val MUSIC_MAKER_PREFERENCES = "music_maker_preferences"
const val CLICKED_SEARCH_TRACK = "clicked_search_track"


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        AppPreferences.setup(applicationContext)
        if (AppPreferences.darkTheme !=null) {
            darkTheme = AppPreferences.darkTheme !!
            switchTheme(darkTheme)
        }


        sendText =  this.getText(R.string.extra_send).toString()
        sendTitle =  this.getText(R.string.send_title).toString()
        extraText = this.getText(R.string.extra_text).toString()
        extraMail = this.getText(R.string.extra_mail).toString()
        extraSubject = this.getText(R.string.extra_subject).toString()
        oferUrl = this.getText(R.string.ofer_url).toString()
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


//    fun getSettingsRepository(): SettingsRepositoryImpl {
//        return SettingsRepositoryImpl()
//    }
//
//    fun getExternalNavigator(): ExternalNavigatorImpl {
//        return ExternalNavigatorImpl(this)
//    }
//
//    fun provideSettingsInteractor(): SettingsInteractor {
//        return SettingsInteractorImpl(getSettingsRepository())
//    }
//
//    fun provideSharingInteractor(): SharingInteractor {
//        return SharingInteractorImpl(getExternalNavigator())
//    }


    companion object {

        var historyTracks= arrayListOf<TrackModel>()
        var playedTracks = arrayListOf<TrackModel>()
        var darkTheme = false
        var sendText = ""
        var sendTitle = ""
        var extraText = ""
        var extraMail = ""
        var extraSubject = ""
        var oferUrl = ""
    }
}

