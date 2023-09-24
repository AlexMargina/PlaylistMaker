package com.example.playlistmaker.search.domain

interface SearchRepository {
    var tracksLoadResultListener: ResultLoad?

    fun loadTracks(query: String)
}