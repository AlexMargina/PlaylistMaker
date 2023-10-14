package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TracksSearchRequest


class RetrofitNetworkClient(
    private val iTunesService: ITunesSearchApi,
    private val context: Context
) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        try {
            if (!isOnline(context)) {
                return Response().apply { resultCode = -1 }
            }
            Log.d ("MAALMI_Retrofit", "dto (${dto.toString()})")
            if (dto is TracksSearchRequest) {
                val response = iTunesService.search(dto.expression).execute()
                Log.d ("MAALMI_Retrofit", "response (${response.toString()})")
                val body = response.body() ?: Response()
                Log.d ("MAALMI_Retrofit", "body (${body.toString()})")
                return body.apply { resultCode = response.code()
                    Log.d ("MAALMI_Retrofit", "resultCode (${resultCode.toString()})")
                }
            } else {
                return Response().apply { resultCode = 400
                }
            }
        }
         catch (error: Exception) {
            throw Exception(error)
             Log.d ("MAALMI_Retrofit", "Error ($error)")
        }
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
