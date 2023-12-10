package com.example.playlistmaker.di

import com.example.playlistmaker.main.ui.MainViewModel
import com.example.playlistmaker.media.ui.favorite.FavoriteViewModel
import com.example.playlistmaker.media.ui.playlist.PlaylistViewModel
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.ui.PlayerViewModel
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.setting.ui.SettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel ()
    }

    viewModel {
        PlayerViewModel(
            get<MediaPlayerInteractor>()
        )
    }

    viewModel {
        SettingViewModel(
            get(),
            get()
        )
    }

    viewModel {
        SearchViewModel(
           get()
        )
    }

    viewModel {
        FavoriteViewModel(androidContext(), get())
    }

    viewModel {
        PlaylistViewModel()
    }


}