package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.dto.ITunesResponse
import retrofit2.Call
import retrofit2.http.*


interface ITunesSearchApi {
    @GET("/search?entity=song")
    fun searchSongApi(@Query("term") text: String): Call<ITunesResponse>
}



