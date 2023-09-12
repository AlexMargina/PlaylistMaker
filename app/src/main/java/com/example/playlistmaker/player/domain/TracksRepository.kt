package com.example.playlistmaker.player.domain

import com.example.playlistmaker.sharing.domain.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}