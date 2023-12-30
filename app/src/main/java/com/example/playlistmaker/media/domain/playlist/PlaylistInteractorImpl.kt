package com.example.playlistmaker.media.domain.playlist


import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun insertPlaylist(playlist: Playlist) {
        repository.insertPlaylist(playlist)
    }

    override suspend fun addNewTrack(track: TrackModel, playlist: Playlist) {
        repository.addNewTrack(track, playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylists()
    }

    override suspend fun getPlaylistById(idPl: Int): Playlist {
        return repository.getPlaylistById(idPl)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, idPl: Int) {
        return repository.deleteTrackFromPlaylist(trackId, idPl )
    }


}