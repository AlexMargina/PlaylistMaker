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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT * FROM track_table  WHERE isFavorite=1 ORDER BY inDbTime DESC")
    suspend fun getFavoriteTracksByTime(): List<TrackEntity>

    @Query("SELECT * FROM track_table WHERE isFavorite=1 AND trackId = :trackId")  // выбор только isFavorite
    suspend fun getFavoriteTrack (trackId : String) : List<TrackEntity>

    @Query("UPDATE track_table SET isFavorite=0 WHERE trackId = :trackId")
    suspend fun deleteTrackFromFavorite (trackId: String)

    @Query("DELETE FROM track_table WHERE isFavorite=0 AND trackId = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Query("DELETE FROM track_table WHERE isFavorite=1 AND trackId = :trackId"  )
    suspend fun deleteFavoriteTrack(trackId: String)
}
