package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.main.ui.MainViewModel
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.ui.MediaViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.setting.ui.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel ()
    }

    viewModel {
        MediaViewModel(
            get<MediaPlayerInteractor>(),
            get<Context>()
        )
    }

    viewModel {
        SettingViewModel(
            sharingInteractor = get(),
            settingsInteractor = get()
        )
    }

    viewModel {
        SearchViewModel(
           get()
        )
    }
}