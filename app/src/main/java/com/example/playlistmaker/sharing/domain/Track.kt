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

//fun makeArrayList () = arrayListOf <Track>(
//    Track(
//        "123",
//        "Smells Like Teen Spirit",
//        "Nirvana",
//        1111,
//        "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg",
//        "Billie Jean",
//        "Michael Jackson",
//        "22222",
//        "https:e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg)",
//        "SUA"
//    ),
//            Track(
//            "123",
//    "Smells Like Teen Spirit",
//    "Nirvana",
//    1111,
//    "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg",
//    "Billie Jean",
//    "Michael Jackson",
//    "22222",
//    "https:e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg)",
//    "SUA"
//)
//)
