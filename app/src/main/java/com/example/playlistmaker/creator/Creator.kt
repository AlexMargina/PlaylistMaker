package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.player.data.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.search.data.HistorySearchDataStoreImpl
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.network.ITunesSearchApi
import com.example.playlistmaker.search.domain.HistorySearchDataStore
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorImpl
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.sharing.domain.CLICKED_SEARCH_TRACK
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object Creator {
    lateinit var application: Application

        private fun provideSearchInteractor(context: Context): SearchInteractor {
            return SearchInteractorImpl(
                getHistorySearchDataStore(context),
                getSearchRepository()
            )
        }

        private fun getHistorySharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(
                CLICKED_SEARCH_TRACK,
                AppCompatActivity.MODE_PRIVATE
            )
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

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(MediaPlayerRepositoryImpl())
    }
    }






/*
1. В Creator создать lateinit var переменную: lateinit var application: Application
2. В Creator создать функцию registryApplication(application: Application), которая будет обновлять переменную application
3. В классе, который является наследником Application, должен был использоваться для обновления темы, в методе onCreate нужно зарегистрировать контекст приложения, то есть вызвать Creator.registryApplication(this)
4. В репозитории вместо Context использовать Application
*/
