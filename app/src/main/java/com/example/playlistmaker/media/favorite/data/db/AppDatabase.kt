package com.example.playlistmaker.media.favorite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.favorite.data.dao.TrackDao
import com.example.playlistmaker.media.favorite.data.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun trackDao(): TrackDao
}
