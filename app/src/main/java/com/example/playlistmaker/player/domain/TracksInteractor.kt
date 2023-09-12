package com.example.playlistmaker.player.domain

import com.example.playlistmaker.sharing.domain.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    fun loadTrackData(trackId: String, onComplete: (Any) -> Unit)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
    }
}