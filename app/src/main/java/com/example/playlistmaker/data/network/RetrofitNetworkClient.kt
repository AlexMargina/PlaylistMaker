package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

 class RetrofitNetworkClient: NetworkClient {
      var resultCode: Int = 0
     private val iTunesBaseUrl = "https://itunes.apple.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)

    override fun doRequest(dto: Any): Response {
        return if (dto is TracksSearchRequest) ({
            val resp = iTunesService.searchSongApi(dto.expression).execute()
            val body = resp.body() ?: com.example.playlistmaker.data.dto.Response()
            body.apply { resultCode = resp.code() }
        }) as Response else {
            com.example.playlistmaker.data.dto.Response().apply { resultCode = HttpURLConnection.HTTP_BAD_REQUEST }
        }
    }

}

