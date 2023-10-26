package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.TrackModel
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var buttonPlay: MaterialButton
    private lateinit var binding: ActivityPlayerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


            buttonPlay = binding.btPlay

            viewModel.observatorScreen().observe(this) {
                refreshScreen(it)
            }

            viewModel.observatorTimer().observe(this) {
                refreshTime(it)
            }


            assign(getTrack())

            buttonPlay.setOnClickListener {
                if (viewModel.isClickAllowed()) {
                    viewModel.playbackControl()
                }
            }

            binding.ivBack.setOnClickListener { finish() }
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

                else -> {
                    buttonPlay.setIconResource(R.drawable.button_play_night)

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

                else -> {
                    buttonPlay.setIconResource(R.drawable.button_play_day)

                }
            }
        }
    }


    fun getTrack(): TrackModel {
        return viewModel.getTrack()
    }


    private fun assign(playedTrack: TrackModel) {

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