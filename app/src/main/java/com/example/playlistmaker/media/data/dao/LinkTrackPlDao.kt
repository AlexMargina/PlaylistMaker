package com.example.playlistmaker.media.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.entity.LinkTrackPlEntity

@Dao
interface LinkTrackPlDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLinkPl (linkTrackPl: LinkTrackPlEntity)

    @Query("DELETE FROM trackid_idpl_table WHERE idPl = :idPl AND trackId = :trackId")
    suspend fun deleteLinkTrackPl(trackId: String, idPl: Int)

    @Query("DELETE FROM trackid_idpl_table WHERE  (((idPl) Not In (SELECT t1.idPl FROM playlist_table AS t1)))")
    suspend fun deleteLinkPl()

    @Query("DELETE FROM track_table WHERE (((trackId) Not In (SELECT t2.trackId FROM trackId_idpl_table AS t2)))")
    suspend fun deleteOrfanTrack()
}