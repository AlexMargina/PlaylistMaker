package com.example.playlistmaker.media.domain.playlist

import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {


    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePl (idPl : Int)

    suspend fun addNewTrack(track: TrackModel, playlist: Playlist)

    suspend fun getPlaylists(): Flow<List<Playlist>>

    suspend fun getPlaylistById (idPl : Int) : Playlist

    suspend fun deleteTrackFromPlaylist(trackId: String, idPl : Int  )

    suspend fun updatePlaylist(idPl: Int?, namePl: String?, imagePl: String?, descriptPl: String?)
}