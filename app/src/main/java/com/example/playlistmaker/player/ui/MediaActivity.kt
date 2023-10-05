package com.example.playlistmaker.player.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.TrackSearchModel
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Locale


class MediaActivity : AppCompatActivity() {


    lateinit var buttonPlay: MaterialButton
    private lateinit var binding: ActivityMediaBinding
    private lateinit var viewModel: MediaViewModel


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

    try {
        buttonPlay = binding.btPlay

        viewModel = ViewModelProvider(this, MediaViewModel.getViewModelFactory())[MediaViewModel::class.java]

        viewModel.observatorScreen().observe(this) {
            refreshScreen(it)
            Log.d("Maalmi", "Изменения экрана во ViewModel ${this.toString()}")
        }

        viewModel.observatorTimer().observe(this) {
            refreshTime(it)
            Log.d("Maalmi", "Изменения времени во ViewModel ${this.toString()}")
        }


            asign(getTrack())



        buttonPlay.setOnClickListener {
            if (viewModel.isClickAllowed()) {
                viewModel.playbackControl()
            }
        }

        binding.ivBack.setOnClickListener { finish() }
    } catch (e: Error) {
        Toast
            .makeText(this, "Выберите сначала песню!", Toast.LENGTH_SHORT)
            .show()
    }

    }

    override fun onDestroy() {
        super.onDestroy()
      //  viewModel.onPause()
    }


    override fun onPause() {
        super.onPause()
     //   viewModel.onPause()
    }


    fun refreshTime(time: String) {
        binding.tvPlaybackTime.text = time
    }


         private fun refreshScreen(state: PlayerState) {
             if (viewModel.isNightTheme()) {
                 when (state) {
                     is PlayerState.PLAYING -> {
                         buttonPlay.setIconResource(R.drawable.button_pause_night)
                     }

                     is PlayerState.PAUSED -> {
                         buttonPlay.setIconResource(R.drawable.button_play_night)
                     }

                     is PlayerState.PREPARED -> {
                         buttonPlay.setIconResource(R.drawable.button_play_night)
                         buttonPlay.isEnabled = true
                     }

                     else -> {buttonPlay.setIconResource(R.drawable.button_play_night)

                     }
                 }
             } else {
                 when (state) {
                     is PlayerState.PLAYING -> {
                         buttonPlay.setIconResource(R.drawable.button_pause_day)
                     }

                     is PlayerState.PAUSED -> {
                         buttonPlay.setIconResource(R.drawable.button_play_day)
                     }

                     is PlayerState.PREPARED -> {
                         buttonPlay.setIconResource(R.drawable.button_play_day)
                         buttonPlay.isEnabled = true
                     }

                     else -> {buttonPlay.setIconResource(R.drawable.button_play_day)

                     }
                 }
             }
        }



     fun getTrack() : TrackSearchModel {
         return viewModel.getTrack()
    }



    private fun asign(playedTrack: TrackSearchModel) {

            val duration = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(playedTrack.trackTimeMillis)
            binding.apply {
                tvTitle.setText(playedTrack.trackName)
                tvArtist.setText(playedTrack.artistName.toString())
                tvPlaybackTime.setText(R.string.null_time)
                tvDuration.setText(duration)
                tvAlbum.setText(playedTrack.collectionName)
                tvYear.setText(playedTrack.releaseDate.substring(0, 4))
                tvGenre.setText(playedTrack.primaryGenreName)
                tvCountry.setText(playedTrack.country)
            }

            val coverUrl100 = playedTrack.artworkUrl100
            val coverUrl500 = coverUrl100.replaceAfterLast('/', "512x512bb.jpg")
            val radius = resources.getDimensionPixelSize(R.dimen.corner_radius)
            Glide.with(binding.ivCover512)
                .load(coverUrl500)
                .transform(RoundedCorners(radius))
                .placeholder(R.drawable.media_placeholder)
                .into(binding.ivCover512)
            buttonPlay.isEnabled = false
    }

}