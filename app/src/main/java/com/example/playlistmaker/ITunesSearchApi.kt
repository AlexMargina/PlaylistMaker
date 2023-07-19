package com.example.playlistmaker

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


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


    fun searchByText(searchedText: String,  onSearchListener : OnSearchListener): ArrayList<Track> {
        onSearchListener.onSearch(searchedText.length)

        iTunesService.run {
            searchSongApi(searchedText).enqueue( object :
                Callback<ITunesResponse> {


                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>
                ) {
                    call.timeout().deadline (5,TimeUnit.SECONDS)
                    tracksITunes = response.body()?.results!!
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    call.timeout().deadline(5,TimeUnit.SECONDS)
                }
            })
        }

        return tracksITunes
    }

    class OnSearchListener(val searchListener: (position: Int) -> Unit) {
        fun onSearch(position: Int) = searchListener(position)
    }
}