package com.example.playlistmaker.sharing.data

import android.content.SharedPreferences
import com.example.playlistmaker.CLICKED_SEARCH_TRACK
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.search.data.SearchDataStorage
import com.example.playlistmaker.search.data.dto.TrackDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


@Suppress("UNCHECKED_CAST")
class SharedPrefsUtils(
    private val appDatabase: AppDatabase,
    private val sharedPref: SharedPreferences,
    private val gson: Gson) : SearchDataStorage {


    override suspend fun getSearchHistory() = readClickedSearchSongs()

    override suspend fun clearHistory() {
        readClickedSearchSongs().clear()
        writeClickedSearchSongs(readClickedSearchSongs())
    }

    override suspend fun addTClickedSearchSongs(track: TrackDto) {
        if (readClickedSearchSongs().contains(track)) { readClickedSearchSongs().remove(track) }

        while (readClickedSearchSongs().size >= 10) { readClickedSearchSongs().removeLast()  }

        readClickedSearchSongs().add(0, track)
        writeClickedSearchSongs(readClickedSearchSongs())
    }

    private fun writeClickedSearchSongs(savedSongs: ArrayList<TrackDto>) {
        sharedPref
            .edit()
            .clear()
            .putString(
                CLICKED_SEARCH_TRACK, gson.toJson(savedSongs))
            .apply()
    }

     suspend fun readClickedSearchSongs(): ArrayList<TrackDto> {
        val json = sharedPref.getString(CLICKED_SEARCH_TRACK, null) ?: return ArrayList()
        val type: Type = object : TypeToken<ArrayList<TrackDto?>?>() {}.type
        val readResult = gson.fromJson<Any>(json, type) as ArrayList<TrackDto>
        val resultAddFavorite = readResult.map {
            TrackDto(
                trackId= it.trackId,
                artistName= it.artistName,
                trackName=it.trackName,
                country=it.country,
                artworkUrl100=it.artworkUrl100,
                collectionName=it.collectionName,
                previewUrl=it.previewUrl,
                primaryGenreName=it.primaryGenreName,
                releaseDate=it.releaseDate,
                trackTimeMillis=it.trackTimeMillis,
                isFavorite = checkIsFavorite(it.trackId))
        }
        return resultAddFavorite as ArrayList<TrackDto>
    }
    suspend fun checkIsFavorite (trackId: String) : Boolean {
        return  (appDatabase.trackDao().getFavoriteTrack(trackId).size>0)
    }
}

