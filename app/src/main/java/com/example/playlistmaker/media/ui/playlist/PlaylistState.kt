package com.example.playlistmaker.media.ui.playlist

import com.example.playlistmaker.media.domain.Playlist

sealed class PlaylistState {
    class Playlists(val playlists: List<Playlist>) : PlaylistState()
    object Empty : PlaylistState()
}