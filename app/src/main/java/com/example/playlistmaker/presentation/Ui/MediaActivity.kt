package com.example.playlistmaker.presentation.Ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.App
import com.example.playlistmaker.domain.PlayerState
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale


class MediaActivity : AppCompatActivity() {


    lateinit var  buttonPlay : MaterialButton
    val handler = Handler(Looper.getMainLooper())
    private val mediaPlayerInteractor = Creator.provideMediaPlayerInteractor()
    private val RefreshDelayMs = 333L

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

         // Элементы экрана:
        val backOffImage = findViewById<ImageView>(R.id.iv_back)
        val cover = findViewById<ImageView>(R.id.iv_cover512)
        val title = findViewById<TextView>(R.id.tv_title)
        val artist = findViewById<TextView>(R.id.tv_artist)
        val buttonAdd = findViewById<ImageView>(R.id.iv_add)
         buttonPlay = findViewById<MaterialButton>(R.id.bt_play)
        val buttonLike = findViewById<ImageView>(R.id.iv_like)
        val playback = findViewById<TextView>(R.id.tv_playback_time)
        val durationTrack = findViewById<TextView>(R.id.tv_duration)
        val album = findViewById<TextView>(R.id.tv_album)
        val yearTrack = findViewById<TextView>(R.id.tv_year)
        val genre = findViewById<TextView>(R.id.tv_genre)
        val country = findViewById<TextView>(R.id.tv_country)

        backOffImage.setOnClickListener { finish() }


        if (App.activeTracks.size > 0) {
            val playedTrack = App.activeTracks[0]

            val duration = SimpleDateFormat("mm:ss", Locale.getDefault() )
                .format(playedTrack.trackTimeMillis)
                title.setText(playedTrack.trackName)
                artist.setText(playedTrack.artistName)
                playback.setText("0:00")
                durationTrack.setText(duration)
                album.setText(playedTrack.collectionName)
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
            val trackViewUrl = playedTrack.previewUrl
            buttonPlay.isEnabled = false
            mediaPlayerInteractor.preparePlayer(trackViewUrl,
                {buttonPlay.isEnabled = true},
                {if (App.darkTheme) {buttonPlay.setIconResource(R.drawable.button_play_night)}
                else {buttonPlay.setIconResource(R.drawable.button_play_day)}
                }
            )
        }

         fun refreshTime() {
            val timeThread = Thread {
                handler.postDelayed(
                    object : Runnable {
                        override fun run() {
                            val trackPosition = SimpleDateFormat("mm:ss", Locale.getDefault() )
                                .format(mediaPlayerInteractor.currentPosition())
                            playback.setText(trackPosition)

                            handler.postDelayed(this,RefreshDelayMs)
                            if (mediaPlayerInteractor.playerState == PlayerState.PREPARED) {playback.setText("00:00")}
                        }
                    },   RefreshDelayMs
                )
            }.start()
        }

        fun switchImagesPausePlay() {
            if (mediaPlayerInteractor.playerState == PlayerState.PLAYING) {
                if (App.darkTheme) {buttonPlay.setIconResource(R.drawable.button_play_night )}
                else {buttonPlay.setIconResource(R.drawable.button_play_night)}
            } else {
                if (App.darkTheme) {buttonPlay.setIconResource(R.drawable.button_pause_night)}
                else {buttonPlay.setIconResource(R.drawable.button_pause_night)}
            }
        }


        buttonPlay.setOnClickListener {
            mediaPlayerInteractor.playbackControl({ switchImagesPausePlay() },
                { switchImagesPausePlay() })
            if (mediaPlayerInteractor.playerState == PlayerState.PLAYING) {refreshTime()}
        }
    }


    override fun onDestroy(){
        super.onDestroy()

        mediaPlayerInteractor.pausePlayer( { })

    }


    override fun onPause() {
        super.onPause()

        mediaPlayerInteractor.pausePlayer( { })
        if (App.darkTheme) {buttonPlay.setIconResource(R.drawable.button_play_night)}
        else {buttonPlay.setIconResource(R.drawable.button_play_day)}
    }
}
