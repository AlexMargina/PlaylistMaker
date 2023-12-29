package com.example.playlistmaker.media.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.media.data.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlist_table WHERE idPl = :idPl")
    suspend fun getPlaylistById(idPl : Int): PlaylistEntity

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPl(playlist: PlaylistEntity)

    @Update( onConflict = OnConflictStrategy.REPLACE, entity = PlaylistEntity::class)
    suspend fun updatePl(playlist: PlaylistEntity)

}