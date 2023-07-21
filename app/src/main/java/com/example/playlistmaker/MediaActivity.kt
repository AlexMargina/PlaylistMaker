package com.example.playlistmaker

import android.app.Application
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale


class MediaActivity : AppCompatActivity() {

        fun readClickedSavedSongs() : ArrayList<Track> {
            val sharedPrefsApp = getSharedPreferences(MUSIC_MAKER_PREFERENCES, Application.MODE_PRIVATE)
            val jsonString = sharedPrefsApp.getString(CLICKED_SEARCH_TRACK, null)
            val json = GsonBuilder().create()
            val clickedSearchSongs = json
                .fromJson(jsonString, object: TypeToken<ArrayList<Track>>(){ }
                .type) ?: arrayListOf<Track>()
            return clickedSearchSongs ?: arrayListOf()
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_media)

            // Элементы экрана:
            val backOffImage = findViewById<ImageView>(R.id.iv_back)
            val cover =  findViewById<ImageView>(R.id.iv_cover512)
            val title = findViewById<TextView>(R.id.tv_title)
            val artist= findViewById<TextView>(R.id.tv_artist)
            val buttonAdd= findViewById<ImageView>(R.id.iv_add)
            val buttonPlay = findViewById<ImageView>(R.id.iv_play)
            val buttonLike = findViewById<ImageView>(R.id.iv_like)
            val playback = findViewById<TextView>(R.id.tv_playback_time)
            val durationTrack = findViewById<TextView>(R.id.tv_duration)
            val album = findViewById<TextView>(R.id.tv_album)
            val yearTrack = findViewById<TextView>(R.id.tv_year)
            val genre = findViewById<TextView>(R.id.tv_genre)
            val country = findViewById<TextView>(R.id.tv_country)

            backOffImage.setOnClickListener { finish() }

            if (readClickedSavedSongs().size>0) {
                val playedTrack = readClickedSavedSongs()[0]
                val duration = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(playedTrack.trackTimeMillis)
                title.setText(playedTrack.trackName)
                artist.setText(playedTrack.artistName)
                playback.setText("0:00")  //для примера 0:00
                durationTrack.setText(duration)
                album.setText(playedTrack.collectionName.toString())
                yearTrack.setText(playedTrack.releaseDate.substring(0, 4))
                genre.setText(playedTrack.primaryGenreName)
                country.setText(playedTrack.country)
                val coverUrl100 = playedTrack.artworkUrl100
                val coverUrl500 = coverUrl100.replaceAfterLast('/', "512x512bb.jpg")
                val radius = resources.getDimensionPixelSize(R.dimen.corner_radius)
                Glide.with(cover)
                    .load(coverUrl500)
                    .transform(RoundedCorners(radius))
                    .placeholder(R.drawable.media_placeholder)
                    .into(cover)
            }
            buttonPlay.setOnClickListener({playback.setText("0:30")})  //контроль работы кнопки, заменить в следующем спринте

    }
}


