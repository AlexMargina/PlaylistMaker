package com.example.playlistmaker.search.domain

import com.bumptech.glide.util.Executors
import com.example.playlistmaker.player.domain.api.TracksInteractor
import com.example.playlistmaker.player.domain.api.TracksRepository



class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.mainThreadExecutor()
    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
    }

    override fun loadTrackData(trackId: String, onComplete: (Any) -> Unit) {

    }
}