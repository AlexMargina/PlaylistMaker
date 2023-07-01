package com.example.playlistmaker

/*
Определение формата и содержания музыкальных записей.  Для этого:
- определим класс Track (Название композиции, Имя исполнителя, Продолжительность трека, Ссылка на изображение обложки)
- создадим список записей в функции makeArrayList (): ArrayList<Track>
 */
data class Track (
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val trackViewUrl: String  // Ссылка на трэк в iTunes
)