package com.example.playlistmaker.di

import com.example.playlistmaker.media.data.FavoriteRepositoryImpl
import com.example.playlistmaker.media.data.NewPlaylistRepositoryImpl
import com.example.playlistmaker.media.data.convertor.TrackDbConvertor
import com.example.playlistmaker.media.domain.favorite.FavoriteRepository
import com.example.playlistmaker.media.domain.newPlaylist.NewPlaylistRepository
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.data.SearchDataStorage
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.SharedPrefsUtils
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.setting.data.SettingsRepositoryImpl
import com.example.playlistmaker.setting.domain.SettingsRepository
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.dsl.module

val repositoryModule = module {

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single <SearchDataStorage> {
        SharedPrefsUtils(get(), get(), get())
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get(), get(), get(), get())
    }

    factory { TrackDbConvertor() }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single <NewPlaylistRepository> {
        NewPlaylistRepositoryImpl (get())
    }
}