package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class RetrofitNetworkClient(
    private val iTunesService: ITunesSearchApi,
    private val context: Context
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {

        if (! isOnline(context)) {
            return Response().apply { resultCode = - 1 }
        }
        Log.d("MAALMI_Retrofit", "dto (${dto.toString()})")
        if (dto is TracksSearchRequest) {
            return withContext(Dispatchers.IO) {
                try {
                    val response = iTunesService.search(dto.expression)
                    response.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 500 }
                }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }


    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("MAALMI", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("MAALMI", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("MAALMI", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }
}
