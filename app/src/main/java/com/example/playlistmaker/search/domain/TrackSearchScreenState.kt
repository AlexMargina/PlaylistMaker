package com.example.playlistmaker.search.domain

import com.example.playlistmaker.sharing.domain.Track

sealed class TrackSearchScreenState {
    object Loading: TrackSearchScreenState()
    data class Content(
        val trackModel: Track,
    ): TrackSearchScreenState()

}
