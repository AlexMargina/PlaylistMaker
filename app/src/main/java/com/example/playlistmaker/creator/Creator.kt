package com.example.playlistmaker.creator

import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.data.TracksRepositoryImpl
import com.example.playlistmaker.player.data.network.RetrofitNetworkClient
import com.example.playlistmaker.player.data.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.TracksInteractor
import com.example.playlistmaker.player.domain.TracksRepository
import com.example.playlistmaker.search.domain.TracksInteractorImpl


object Creator {
     fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(MediaPlayerRepositoryImpl())
    }


}