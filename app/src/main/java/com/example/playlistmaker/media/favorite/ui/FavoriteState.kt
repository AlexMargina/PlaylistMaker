package com.example.playlistmaker.media.favorite.ui


import com.example.playlistmaker.search.domain.TrackModel

sealed class FavoriteState {

    object Loading : FavoriteState()

    data class Content(
        val tracks: ArrayList<TrackModel>
    ) : FavoriteState()

    data class Empty(
        val message: String
    ) : FavoriteState()
}