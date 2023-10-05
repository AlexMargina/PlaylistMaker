package com.example.playlistmaker.sharing.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.playlistmaker.search.data.SearchDataStorage
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.sharing.domain.CLICKED_SEARCH_TRACK
import com.example.playlistmaker.sharing.domain.MUSIC_MAKER_PREFERENCES
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@Suppress("UNCHECKED_CAST")
class SharedPrefsSearchDataStorage(context: Context) : SearchDataStorage {

    private val sharedPref = context.getSharedPreferences(
        MUSIC_MAKER_PREFERENCES,
        MODE_PRIVATE
    )

    private val historyList = readFromSharedPref()

    override fun getSearchHistory() = historyList

    override fun clearHistory() {
        historyList.clear()
        updateSharedPref()
    }

    override fun addTrackToHistory(track: TrackDto) {
        if (historyList.contains(track)) {
            historyList.remove(track)
        }
        if (historyList.size >= 10) {
            historyList.removeLast()
        }
        historyList.add(0, track)
        updateSharedPref()
    }

    private fun updateSharedPref() {
        sharedPref.edit().clear().putString(CLICKED_SEARCH_TRACK, Gson().toJson(historyList)).apply()
    }

    private fun readFromSharedPref(): ArrayList<TrackDto> {
        val json = sharedPref.getString(CLICKED_SEARCH_TRACK, null) ?: return ArrayList()
        val type: Type = object : TypeToken<ArrayList<TrackDto?>?>() {}.type
        return Gson().fromJson<Any>(json, type) as ArrayList<TrackDto>
    }


}

