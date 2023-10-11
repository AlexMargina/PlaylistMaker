package com.example.playlistmaker.sharing.domain

data class Track(
    val trackId: String,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val previewUrl: String,  // Ссылка на трэк в iTunes
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
)
