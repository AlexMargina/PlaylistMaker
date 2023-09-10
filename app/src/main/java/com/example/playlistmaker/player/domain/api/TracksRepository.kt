package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.sharing.domain.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}