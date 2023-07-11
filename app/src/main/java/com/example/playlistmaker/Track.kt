package com.example.playlistmaker

data class Track (
    val trackId : Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val trackViewUrl: String  // Ссылка на трэк в iTunes
)
