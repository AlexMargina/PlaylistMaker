package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.data.SearchDataStorage
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.SharedPreferencesSearchHistoryStorage
import com.example.playlistmaker.search.domain.SearchHistoryStorage
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.setting.data.SettingsRepositoryImpl
import com.example.playlistmaker.setting.domain.SettingsRepository
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.SharedPrefsUtils
import org.koin.dsl.module

val repositoryModule = module {

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    factory<ExternalNavigator> {
        ExternalNavigatorImpl(get<Context>())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get<Context>())
    }

    factory <SearchDataStorage> {
        SharedPrefsUtils(get())
    }

    factory<SearchRepository> {
        SearchRepositoryImpl(get(), get())
    }

    single<SearchHistoryStorage> {
        SharedPreferencesSearchHistoryStorage(get(), get())
    }
}