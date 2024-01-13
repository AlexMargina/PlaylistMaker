package com.example.playlistmaker.media.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "trackid_idpl_table")
data class LinkTrackPlEntity(

    @PrimaryKey(autoGenerate = true)
    val idLink: Int,
    val trackId: String,
    val idPl :  Int,
    val inPlTime : Long = Calendar.getInstance().time.time
)