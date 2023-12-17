package com.example.playlistmaker.media.domain


data class Playlist(
    val playlistId: Int,
    val title: String,
    val description: String = "",
    val uriForImage: String = "",
    val tracksId: MutableList<String> = mutableListOf(),
    var countTracks: Int = 0,
)