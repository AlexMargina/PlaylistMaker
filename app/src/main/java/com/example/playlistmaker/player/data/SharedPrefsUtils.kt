package com.example.playlistmaker.player.data

import android.content.SharedPreferences
import com.example.playlistmaker.sharing.domain.Track
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


class SharedPrefsUtils(val sharedPref: SharedPreferences)  {


    fun writeClickedSearchSongs(keyFiles: String, savedSongs: ArrayList<Track>) {

        val json = GsonBuilder().create()
        val jsonString = json.toJson(savedSongs)
        sharedPref.edit()
            .putString(keyFiles, jsonString)
            .apply()
    }

    fun readClickedSearchSongs(keyFiles: String) : ArrayList<Track> {

        val jsonString = sharedPref.getString(keyFiles, null)
        val json = GsonBuilder().create()
        val savedSongs : ArrayList<Track> = json.fromJson(jsonString, object: TypeToken<ArrayList<Track>>() { }.type) ?: arrayListOf()

        return savedSongs
    }
}

