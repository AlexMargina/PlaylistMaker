package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.media.domain.playlist.PlaylistRepository
import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow

class PlaylistRepositoryImpl (appDatabase: AppDatabase) : PlaylistRepository {
    override suspend fun createPlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override suspend fun addNewTrack(track: TrackModel, playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        TODO("Not yet implemented")
    }

}
