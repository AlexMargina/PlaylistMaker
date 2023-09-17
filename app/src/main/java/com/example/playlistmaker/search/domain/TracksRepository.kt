package com.example.playlistmaker.search.domain

import com.example.playlistmaker.sharing.domain.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}