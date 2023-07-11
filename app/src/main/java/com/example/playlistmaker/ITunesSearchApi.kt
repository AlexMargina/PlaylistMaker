package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.*

interface ITunesSearchApi {
    @GET("/search?entity=song")
    fun searchSongApi(@Query("term") text: String): Call<ITunesResponse>
}
class ITunesResponse (  val resultCount: Int,
                        var results : ArrayList<Track>)
