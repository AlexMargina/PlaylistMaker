package com.example.playlistmaker.search.data.network

import android.content.Context
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.HttpsURLConnection

class RetrofitNetworkClient(
    private val context: Context
) : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesSearchApi::class.java)

    override fun doRequest(dto: Any): Response {

        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = HttpsURLConnection.HTTP_BAD_REQUEST }
        }

        val response = iTunesService.search(dto.expression).execute()
        val body = response.body()

        return body?.apply { resultCode = response.code() } ?: Response().apply {
            resultCode = response.code()
        }
    }
}
