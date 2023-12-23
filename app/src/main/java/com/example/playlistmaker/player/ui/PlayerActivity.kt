package com.example.playlistmaker.player.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.media.ui.playlist.PlaylistState
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.TrackModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: ActivityPlayerBinding
    private val adapter = PlayerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomSheetGroup = binding.playlistsBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetGroup)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.recyclerAddToPlaylist.layoutManager = GridLayoutManager(this, 1)
        binding.recyclerAddToPlaylist.adapter = adapter

        viewModel.observePlayerState().observe(this) {
            refreshTime(it.progress)
            refreshScreen(it)
        }

        viewModel.playlistsLiveData.observe(this)  { playlistState ->
            displaylist(playlistState)
        }



        binding.btPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.ivBack.setOnClickListener { finish() }

        binding.ivLike.setOnClickListener {
            viewModel.likeOrDislike()

            if (getTrack().isFavorite) {
                binding.ivLike.setImageResource(R.drawable.buttonlike)
            } else {
                binding.ivLike.setImageResource(R.drawable.buttondislike)
            }
        }

        assign(getTrack())

        binding.ivAdd.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.showPlaylist()

            viewModel.playlistsLiveData.observe(this) { playlistState ->displaylist(playlistState)

                }
            }
        }

    private fun displaylist(playlistState: PlaylistState) {
        when (playlistState) {
            PlaylistState.Empty -> {}
            is PlaylistState.Playlists -> {
                adapter.playlists.addAll(playlistState.playlists)
                adapter.notifyDataSetChanged()
            }
        }
    }



    fun refreshTime(time: String) {
        if (viewModel.observePlayerState().value != PlayerState.PREPARED()) {binding.tvPlaybackTime.text = time}
    }


    private fun refreshScreen(state: PlayerState) {
        binding.btPlay.isEnabled = state.isPlayButtonEnabled
        if (viewModel.isNightTheme()) {
            when (state) {
                is PlayerState.PLAYING -> {
                    binding.btPlay.setIconResource(R.drawable.button_pause_night)
                }

                is PlayerState.PAUSED -> {
                    binding.btPlay.setIconResource(R.drawable.button_play_night)
                }

                is PlayerState.PREPARED -> {
                    binding.btPlay.setIconResource(R.drawable.button_play_night)
                    binding.btPlay.isEnabled = true
                    binding.tvPlaybackTime.setText(R.string.null_time)
                }

                else -> {
                    binding.btPlay.setIconResource(R.drawable.button_play_night)
                    binding.tvPlaybackTime.setText(R.string.null_time)
                }
            }
        } else {
            when (state) {
                is PlayerState.PLAYING -> {
                    binding.btPlay.setIconResource(R.drawable.button_pause_day)
                }

                is PlayerState.PAUSED -> {
                    binding.btPlay.setIconResource(R.drawable.button_play_day)
                }

                is PlayerState.PREPARED -> {
                    binding.btPlay.setIconResource(R.drawable.button_play_day)
                    binding.btPlay.isEnabled = true
                    binding.tvPlaybackTime.setText(R.string.null_time)
                }

                else -> {
                    binding.btPlay.setIconResource(R.drawable.button_play_day)
                    binding.tvPlaybackTime.setText(R.string.null_time)
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
            if (playedTrack.isFavorite) ivLike.setImageResource(R.drawable.buttonlike)
        }

        val coverUrl100 = playedTrack.artworkUrl100
        val coverUrl500 = coverUrl100.replaceAfterLast('/', "512x512bb.jpg")
        val radius = resources.getDimensionPixelSize(R.dimen.corner_radius)
        Glide.with(binding.ivCover512)
            .load(coverUrl500)
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.media_placeholder)
            .into(binding.ivCover512)
        binding.btPlay.isEnabled = false
    }
}