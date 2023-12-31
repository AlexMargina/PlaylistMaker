package com.example.playlistmaker.utils

class Converters {

    fun convertCountToTextTracks (countTracks: Int): String {

        val s = when (countTracks % 10) {
            1 -> "$countTracks трек"
            2, 3, 4 -> "$countTracks трека"
            else -> "$countTracks треков"
        }
        return s
    }
}