package com.example.playlistmaker.player.data.network

import com.example.playlistmaker.player.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}