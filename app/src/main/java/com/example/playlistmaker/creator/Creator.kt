package com.example.playlistmaker.creator

import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.data.TracksRepositoryImpl
import com.example.playlistmaker.player.data.network.RetrofitNetworkClient
import com.example.playlistmaker.player.domain.MediaPlayerInteractorImpl
import com.example.playlistmaker.player.domain.api.MediaPlayerInteractor
import com.example.playlistmaker.player.domain.api.TracksInteractor
import com.example.playlistmaker.player.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.TracksInteractorImpl


object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    fun provideMediaPlayerInteractor(): MediaPlayerInteractor {
        return MediaPlayerInteractorImpl(MediaPlayerRepositoryImpl())
    }


}