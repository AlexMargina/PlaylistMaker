package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerRepository
import com.example.playlistmaker.search.data.SearchRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.SearchInteractor
import com.example.playlistmaker.search.domain.SearchInteractorImpl
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.sharing.data.SharedPrefsUtils
import com.example.playlistmaker.sharing.domain.App


object Creator {
    private lateinit var application: App

    fun init(application: App) {
        this.application = application
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(MediaPlayerRepositoryImpl())
    }

    fun provideMediaPlayerRepository(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl()
    }



    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(provideSearchRepository(context))
    }



    private fun provideSearchRepository(context: Context): SearchRepository {
        return SearchRepositoryImpl(
            RetrofitNetworkClient(context),
            SharedPrefsUtils(context),
        )
    }
}

//    private fun provideDataStorage(context: Context): SettingsDataStorage {
//        return SharedPrefSettingsDataStorage(context)
//    }

//    fun provideSettingsInteractor(context: Context): SettingsInteractor {
//        return SettingsInteractorImpl(SettingsRepositoryImpl(provideDataStorage(context)))
//    }

//    fun provideSharingInteractor(context: Context): SharingInteractor {
//        return SharingIntercatorImpl(ExternalNavigatorImpl(context))
//    }




/*
1. В Creator создать lateinit var переменную: lateinit var application: Application
2. В Creator создать функцию registryApplication(application: Application), которая будет обновлять переменную application
3. В классе, который является наследником Application, должен был использоваться для обновления темы, в методе onCreate нужно зарегистрировать контекст приложения, то есть вызвать Creator.registryApplication(this)
4. В репозитории вместо Context использовать Application
*/
