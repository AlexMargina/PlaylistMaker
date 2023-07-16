package com.example.playlistmaker

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ITunesSearchApi {
    @GET("/search?entity=song")
    fun searchSongApi(@Query("term") text: String): Call<ITunesResponse>
}

class ITunesResponse (  val resultCount: Int,
                        var results : ArrayList<Track>)


class ITunesSearch (searchedText: String)
{
        private val iTunesBaseUrl = "https://itunes.apple.com"
        private val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        private val iTunesService = retrofit.create(ITunesSearchApi::class.java)
        var responsResults : ArrayList<Track> = arrayListOf()

        fun searchSongByText(searchedText: String): ArrayList<Track> {
                val response = iTunesService.searchSongApi(searchedText).enqueue(object : Callback<ITunesResponse> {

                    override fun onResponse(call: Call<ITunesResponse>, response: Response<ITunesResponse>)
                    {
                       responsResults = response.body()?.results!!
                     }

                    override fun onFailure(call: Call<ITunesResponse>, t: Throwable) { }

                })

                return responsResults
        }
}