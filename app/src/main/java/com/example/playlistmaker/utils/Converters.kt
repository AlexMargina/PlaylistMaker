package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

class Converters {

    fun convertCountToTextTracks (countTracks: Int): String {

        val s = when (countTracks % 10) {
            1 -> "$countTracks трек"
            2, 3, 4 -> "$countTracks трека"
            else -> "$countTracks треков"
        }
        return s
    }

    fun convertMillisToTextMinutes(ms : Long) : String {
        val minutes = SimpleDateFormat("m", Locale.getDefault()).format(ms)
        return when ( ms/60000L%10) {
            11L  -> "$minutes минут"
            1L -> "$minutes минута"
            2L,3L,4L -> "$minutes минуты"
            else -> {"$minutes минут" }
        }



    }


}