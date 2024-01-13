package com.example.playlistmaker.utils


import android.content.Context
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

class Converters (val context:Context){

    val context1 = android.R.string()

    fun convertCountToTextTracks(countTracks: Int): String {
        var s = ""
        if (countTracks in 5..20) return "$countTracks ${context.getText(R.string.tracks)}"
        return try {
            s = when (countTracks % 10) {
                1 -> "$countTracks ${context.getString(R.string.track) }"
                2, 3, 4 -> "$countTracks ${context.getText(R.string.tracks_2)}"
                else -> "$countTracks ${context.getText(R.string.tracks)}"
            }
            s
        } catch (er: Error) {
            er.toString()
        }
    }

    fun convertMillisToTextMinutes(ms: Long): String {
        val minutes = SimpleDateFormat("m", Locale.getDefault()).format(ms)
        if (ms / 60000L in 5..20) return "$minutes ${context.getText(R.string.minutes)}"
        return try {
            when (ms / 60000L % 10) {
                1L -> "$minutes ${context.getText(R.string.minute)}"
                2L, 3L, 4L -> "$minutes ${context.getText(R.string.minutes_2)}"
                else -> {
                    "$minutes ${context.getText(R.string.minutes)}"
                }
            }
        } catch (er: Error) {
            er.toString()
        }
    }
}