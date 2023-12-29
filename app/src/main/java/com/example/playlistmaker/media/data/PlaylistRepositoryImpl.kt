package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.entity.PlaylistEntity
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.media.domain.playlist.PlaylistRepository
import com.example.playlistmaker.search.domain.TrackModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type


class PlaylistRepositoryImpl (val appDatabase: AppDatabase) : PlaylistRepository {

    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPl(convertToEntityPlaylist(playlist))
    }

    override suspend fun addNewTrack(track: TrackModel, playlist: Playlist) {
        playlist.tracksPl.add(0, track)
        playlist.countTracks = playlist.tracksPl.size
        appDatabase.playlistDao().updatePl(convertToEntityPlaylist(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        val playlistFlow = appDatabase.playlistDao().getPlaylists()
        return playlistFlow.map { playlist -> playlist.map { convertToPlaylist(it) } }
    }

    override suspend fun getPlaylistById (idPl : Int) : Playlist {
        val playlist = appDatabase.playlistDao().getPlaylistById (idPl)
        return convertToPlaylist(playlist)
    }

    private fun convertToEntityPlaylist(playlist: Playlist): PlaylistEntity {
        var time_pl : Long = 1110L
        return PlaylistEntity(
            idPl = playlist.idPl,
            namePl = playlist.namePl,
            descriptPl = playlist.descriptPl,
            imagePl = playlist.imagePl,
            tracksPl = convertListToString(playlist.tracksPl),
            countTracksPl = playlist.countTracks,
            timePl = time_pl
        )
    }

    private fun convertListToString(tracksPl: ArrayList<TrackModel>): String {
        return  Gson().toJson(tracksPl)
    }

    private fun convertStringToList(tracksId: String): ArrayList<TrackModel> {
        val type: Type = object : TypeToken<ArrayList<TrackModel?>?>() {}.type
        return Gson().fromJson(tracksId, type) as ArrayList<TrackModel>
    }

    private fun convertToPlaylist(playlist: PlaylistEntity): Playlist {

        return Playlist(
            idPl = playlist.idPl,
            namePl = playlist.namePl,
            descriptPl = playlist.descriptPl,
            imagePl = playlist.imagePl,
            tracksPl = convertStringToList(playlist.tracksPl),
            countTracks = playlist.countTracksPl
        )
    }
}
