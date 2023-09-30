package com.example.playlistmaker.search.domain

import java.text.SimpleDateFormat
import java.util.Locale

data class TrackSearchModel(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) {
    fun getDuration() = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
}