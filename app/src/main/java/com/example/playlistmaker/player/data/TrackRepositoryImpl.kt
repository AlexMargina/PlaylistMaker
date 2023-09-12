package com.example.playlistmaker.player.data

import com.example.playlistmaker.player.data.dto.TracksSearchRequest
import com.example.playlistmaker.player.data.dto.TracksSearchResponse
import com.example.playlistmaker.player.data.network.NetworkClient
import com.example.playlistmaker.sharing.domain.Track
import com.example.playlistmaker.player.domain.TracksRepository
import java.net.HttpURLConnection

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        if (response.resultCode == HttpURLConnection.HTTP_OK) {
            return (response as TracksSearchResponse).results.map {
                Track(
                    it.trackId.toInt(),
                    it.trackName,
                    it.artistName,
                    //it.getDuration(),
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.collectionName,
                    it.getReleaseYear(),
                    it.primaryGenreName,
                    it.country,
                    it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
    }
