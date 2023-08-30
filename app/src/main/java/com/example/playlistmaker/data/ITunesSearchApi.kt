package com.example.playlistmaker.data

import com.example.playlistmaker.domain.Track
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ITunesSearchApi {
    @GET("/search?entity=song")
    fun searchSongApi(@Query("term") text: String): Call<ITunesResponse>
}

class ITunesResponse (  val resultCount: Int,
                        var results : ArrayList<Track>)


class ITunesSearch(searchedText: String, val onSearchListener : OnSearchListener)
{
        private val iTunesBaseUrl = "https://itunes.apple.com"
        private val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        private val iTunesService = retrofit.create(ITunesSearchApi::class.java)
        var tracksITunes :ArrayList <Track> = arrayListOf()



    class OnSearchListener(val searchListener: (position: Int) -> Unit) {
        fun onSearch(position: Int) = searchListener(position)
    }
}