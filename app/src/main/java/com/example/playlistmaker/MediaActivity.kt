package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        //val text = (Base64.getDecoder().decode("WWFuZGV4LkZpbnRlY2guQW5kcm9pZA==").decodeToString())

        val trackId = getIntent().getIntExtra("trackId", 1)


        // Элементы экрана:
        val backOffImage = findViewById<ImageView>(R.id.iv_back)  // стрелка НАЗАД
        val cover =  findViewById<ImageView>(R.id.iv_cover512)
        val title = findViewById<TextView>(R.id.tv_title)
        val  artist= findViewById<TextView>(R.id.tv_artist)
        val buttonAdd= findViewById<ImageView>(R.id.iv_add)
        val buttonPlay = findViewById<ImageView>(R.id.iv_play)
        val buttonLike = findViewById<ImageView>(R.id.iv_like)
        val playback = findViewById<TextView>(R.id.tv_playback_time)
        val durationTrack = findViewById<TextView>(R.id.tv_duration)
        val album = findViewById<TextView>(R.id.tv_album)
        val yearTrack = findViewById<TextView>(R.id.tv_year)
        val genre = findViewById<TextView>(R.id.tv_genre)
        val country = findViewById<TextView>(R.id.tv_country)

        title.setText(trackId.toString())

        backOffImage.setOnClickListener { finish() }


    // END of fun onCreate
    }
}


