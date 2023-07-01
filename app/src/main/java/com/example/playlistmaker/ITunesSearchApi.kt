package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.*
import java.text.SimpleDateFormat
import java.util.Locale


interface ITunesSearchApi {
    @GET("/search?entity=song")
    fun searchSongApi(@Query("term") text: String): Call<ITunesResponse>

}


class ITunesResponse (  val resultCount: Int,
                        var results : ArrayList<Track>)
{
    fun timeTrackFormat(){
        for (track in results)
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(293000L)
    }

}

