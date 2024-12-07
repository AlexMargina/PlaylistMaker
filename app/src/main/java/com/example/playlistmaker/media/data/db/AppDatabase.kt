package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.data.dao.LinkTrackPlDao
import com.example.playlistmaker.media.data.dao.PlaylistDao
import com.example.playlistmaker.media.data.dao.TrackDao
import com.example.playlistmaker.media.data.entity.LinkTrackPlEntity
import com.example.playlistmaker.media.data.entity.PlaylistEntity
import com.example.playlistmaker.media.data.entity.TrackEntity

@Database(version = 2, entities = [TrackEntity::class, PlaylistEntity::class, LinkTrackPlEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun linkTrackPlDao(): LinkTrackPlDao
}