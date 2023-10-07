package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.TrackModel

fun interface TrackClickListener {
    fun onTrackClick(track: TrackModel)
}