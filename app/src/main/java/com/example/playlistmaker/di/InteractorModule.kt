package com.example.playlistmaker.di

import com.example.playlistmaker.media.domain.favorite.FavoriteInteractor
import com.example.playlistmaker.media.domain.favorite.FavoriteInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorImpl
import com.example.playlistmaker.setting.domain.SettingsInteractor
import com.example.playlistmaker.setting.domain.SettingsInteractorImpl
import com.example.playlistmaker.sharing.domain.SharingInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<MediaPlayerInteractor> {
        MediaPlayerInteractorImpl(get(), get(), get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}