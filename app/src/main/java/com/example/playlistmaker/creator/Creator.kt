package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.player.data.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.TracksInteractorImpl
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.sharing.data.ExternalNavigator
import com.example.playlistmaker.sharing.data.ExternalNavigatorImpl


object Creator {
    lateinit var application: Application
    fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(application)
    }

     fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(MediaPlayerRepositoryImpl())
    }

    fun registryApplication (application: Application) {

    }

}
/*
1. В Creator создать lateinit var переменную: lateinit var application: Application
2. В Creator создать функцию registryApplication(application: Application), которая будет обновлять переменную application
3. В классе, который является наследником Application, должен был использоваться для обновления темы, в методе onCreate нужно зарегистрировать контекст приложения, то есть вызвать Creator.registryApplication(this)
4. В репозитории вместо Context использовать Application
*/
