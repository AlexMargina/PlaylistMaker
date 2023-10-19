package com.example.playlistmaker.search.ui

import android.content.Intent
import android.provider.MediaStore.Audio.AudioColumns.TRACK
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.player.ui.PlayerActivity

class OpenOtherActivity(private val activity: AppCompatActivity)  {

    fun runPlayer(trackId: String) {
        val playerIntent = Intent(activity, PlayerActivity::class.java)
        playerIntent.putExtra(TRACK, trackId)
        activity.startActivity(playerIntent)
    }
}