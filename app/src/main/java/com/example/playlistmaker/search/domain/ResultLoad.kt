package com.example.playlistmaker.search.domain

import com.example.playlistmaker.sharing.domain.Track

interface ResultLoad{
    fun onSuccess(tracks: List<Track>)
    fun onError()
}