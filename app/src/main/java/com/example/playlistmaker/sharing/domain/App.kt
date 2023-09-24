package com.example.playlistmaker.sharing.domain

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R
import com.example.playlistmaker.search.data.HistorySearchDataStoreImpl
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.network.ITunesSearchApi
import com.example.playlistmaker.search.domain.HistorySearchDataStore
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorImpl
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.setting.data.AppPreferences
import com.example.playlistmaker.setting.data.SettingsInteractorImpl
import com.example.playlistmaker.setting.data.SettingsRepositoryImpl
import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.SharingInteractorImpl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val MUSIC_MAKER_PREFERENCES = "music_maker_preferences"
const val DARK_THEME_ENABLED = "dark_theme_enabled"
const val CLICKED_SEARCH_TRACK = "clicked_search_track"


class App : Application() {

    companion object {
        var activeTracks = mutableListOf<Track>()
        var darkTheme = false
        var sendText = ""
        var sendTitle = ""
        var extraText = ""
        var extraMail = ""
        var extraSubject = ""
        var oferUrl = ""
    }


 //   lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        AppPreferences.setup(applicationContext)

        if (AppPreferences.darkTheme !=null) {
            darkTheme = AppPreferences.darkTheme !!
            switchTheme(darkTheme)
        }


        //Creator.registryApplication(this)
        sendText =  this.getText(R.string.extra_send).toString()
        sendTitle =  this.getText(R.string.send_title).toString()
        extraText = this.getText(R.string.extra_text).toString()
        extraMail = this.getText(R.string.extra_mail).toString()
        extraSubject = this.getText(R.string.extra_subject).toString()
        oferUrl = this.getText(R.string.ofer_url).toString()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppPreferences.darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }


    fun getSettingsRepository(): SettingsRepositoryImpl {
        return SettingsRepositoryImpl()
    }

    fun getExternalNavigator(): ExternalNavigatorImpl {
        return ExternalNavigatorImpl(this)
    }

     fun provideSearchInteractor(): SearchInteractor {
        return SearchInteractorImpl(
            getHistorySearchDataStore(this),
            getSearchRepository()
        )
    }

    private fun getHistorySharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(CLICKED_SEARCH_TRACK, AppCompatActivity.MODE_PRIVATE)
    }
    private fun getHistorySearchDataStore(context: Context): HistorySearchDataStore {
        return HistorySearchDataStoreImpl(getHistorySharedPreferences(context))
    }

    private fun getSearchRepository(): SearchRepository {
        return SearchRepositoryImpl(getITunesApi())
    }

    private fun getITunesApi(): ITunesSearchApi {

        return Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ITunesSearchApi::class.java)
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator())
    }
}

