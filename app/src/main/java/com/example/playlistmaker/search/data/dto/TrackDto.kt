package com.example.playlistmaker.search.data.dto

import java.io.Serializable


data class TrackDto(

    val trackId: String,
    val trackName: String = "",
    var artistName: String= "",
    var trackTimeMillis: Long= 0L,
    var artworkUrl100: String= "",
    var collectionName: String= "",
    var releaseDate: String= "",
    var primaryGenreName: String= "",
    var country: String= "",
    var previewUrl: String= "",
    val isFavorite: Boolean = false
    ) : Serializable