package com.example.playlistmaker.media.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.playlistmaker.media.data.entity.PlaylistEntity


@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

}