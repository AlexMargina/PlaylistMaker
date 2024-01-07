package com.example.playlistmaker.media.data

import android.util.Log
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
        appDatabase.playlistDao().updatePlaylist(convertToEntityPlaylist(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        val playlistFlow = appDatabase.playlistDao().getPlaylists()
        return playlistFlow.map { playlist -> playlist.map { convertToPlaylist(it) } }
    }

    override suspend fun deletePl (idPl : Int)  {
        appDatabase.playlistDao().deletePl (idPl)
    }

    override suspend fun getPlaylistById (idPl : Int) : Playlist {
        val playlist = appDatabase.playlistDao().getPlaylistById (idPl)
        return convertToPlaylist(playlist)
    }

    override suspend fun updatePl(idPl: Int?, namePl: String?, descriptPl: String?) {
        Log.d ("PlaylistRepositoryImpl", "updatePl idPl= ${idPl} ")  //1
        appDatabase.playlistDao().updatePl(idPl, namePl,  descriptPl)
    }

    override suspend fun deleteTrackFromPlaylist(trackId: String, idPl: Int) {
        val playlist = convertToPlaylist(appDatabase.playlistDao().getPlaylistById(idPl))
        for (track in playlist.tracksPl) {
            if (track.trackId == trackId) {
                playlist.tracksPl.remove(track)
                Log.d ("PlaylistRepositoryImpl", "4. Удалил track с trackId= ${track.trackId} ")  //1
                break
            }
        }
        appDatabase.playlistDao().updatePlaylist(convertToEntityPlaylist(playlist))
    }

    private fun convertToEntityPlaylist(playlist: Playlist): PlaylistEntity {
        var timeAllTracks = 0L
        for (track in playlist.tracksPl) {
            timeAllTracks += track.trackTimeMillis
        }
        return PlaylistEntity(
            idPl = playlist.idPl,
            namePl = playlist.namePl,
            descriptPl = playlist.descriptPl,
            tracksPl = convertListToString(playlist.tracksPl),
            countTracksPl = playlist.tracksPl.size,
            timePl = timeAllTracks
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
            tracksPl = convertStringToList(playlist.tracksPl),
            countTracks = playlist.countTracksPl,
            timePl=playlist.timePl
        )
    }
}
