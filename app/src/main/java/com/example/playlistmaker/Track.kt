package com.example.playlistmaker

import androidx.constraintlayout.widget.Placeholder

/*
Определение формата и содержания музыкальных записей.  Для этого:
- определим класс Track (Название композиции, Имя исполнителя, Продолжительность трека, Ссылка на изображение обложки)
- создадим список записей в функции makeArrayList (): ArrayList<Track>
 */
data class Track (
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека
    val artworkUrl100: String // Ссылка на изображение обложки
)

fun makeArrayList (): ArrayList<Track> {
    val track1 = Track("Smells Like Teen Spirit", "Nirvana","5:01",  "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg")
    val track2 = Track("Billie Jean", "Michael Jackson","4:35",  "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg")
    val track3 = Track("Stayin' Alive", "Bee Gees","4:10",  "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg")
    val track4 = Track("Whole Lotta Love", "Led Zeppelin","5:33",  "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg")
    val track5 = Track("Sweet Child O'Mine", "Guns N' Roses","5:03",  "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")
    val track6 = Track("Smells Like Teen Spirit", "Nirvana","5:01",  "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg")
    val track7 = Track("Billie Jean", "Michael Jackson","4:35",  "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg")
    val track8 = Track("Stayin' Alive", "Bee Gees","4:10",  "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg")
    val track9 = Track("Whole Lotta Love", "Led Zeppelin","5:33",  "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg")
    val track10 = Track("Sweet Child O'Mine", "Guns N' Roses","5:03",  "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg")

    val arrayListSongs = arrayListOf<Track>(track1, track2, track3, track4, track5, track6, track7, track8, track9, track10)
    return arrayListSongs
}

fun setGlide (imageURL : String, placeholder: Placeholder) {
    //lide ()
}