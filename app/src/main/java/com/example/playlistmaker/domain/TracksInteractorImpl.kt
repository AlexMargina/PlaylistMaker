package com.example.playlistmaker.domain

import com.bumptech.glide.util.Executors
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository



class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.mainThreadExecutor()
    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }
}