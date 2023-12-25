package com.example.playlistmaker.media.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteTrack(tracks: List<TrackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)

    @Query("SELECT * FROM track_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM track_table ORDER BY inDbTime DESC")
    suspend fun getTracksByTime(): List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE trackId = :trackId")
    suspend fun getFavoriteTrack (trackId : String) : List<TrackEntity>


    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: String)
}
