package com.example.playlistmaker.player.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.media.ui.playlist.PlaylistState
import com.example.playlistmaker.player.domain.PlayerState
import com.example.playlistmaker.search.domain.TrackModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerFragment : Fragment() {

    private val viewModel by viewModel<PlayerViewModel>()
    private lateinit var binding: FragmentPlayerBinding
    private val adapter = PlayerAdapter()
    private lateinit var bottomSheetBehavior : BottomSheetBehavior<LinearLayout>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetGroup = binding.playlistsBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetGroup)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.recyclerAddToPlaylist.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerAddToPlaylist.adapter = adapter

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            refreshTime(it.progress)
            refreshScreen(it)
        }

        viewModel.playlistsLiveData.observe(viewLifecycleOwner)  { playlistState ->
            displaylist(playlistState)
        }

        viewModel.addLiveData.observe(viewLifecycleOwner) { reply ->
            displayAddReplyToToast(reply)
        }

        binding.btPlay.setOnClickListener { viewModel.playbackControl() }

        binding.ivBack.setOnClickListener { findNavController().navigateUp() }

        binding.ivLike.setOnClickListener { addLikeOrDislike() }

        assign(getTrack())

        binding.ivAdd.setOnClickListener {addTrackToPlaylist()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

        binding.btNewPlaylistPlayer.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
        }

        adapter.clickListener = { playlist ->
            viewModel.run { addTrackInPlaylist(track = getTrack(), playlist = playlist) }
        }
    }


    private fun addTrackToPlaylist (){
        viewModel.getPlaylist()
        viewModel.playlistsLiveData.observe(requireActivity()) {
                playlistState ->displaylist(playlistState)
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

    private fun displayAddReplyToToast(reply: ReplyOnAddTrack) {
        when (reply) {
            is ReplyOnAddTrack.Added -> {
                Toast.makeText(
                    requireActivity(),
                    "Добавлено в плейлист ${reply.playlist.namePl}",
                    Toast.LENGTH_SHORT
                ).show()
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }

            is ReplyOnAddTrack.Contained -> {
                Toast.makeText(
                    requireActivity(),
                    "Трек уже добавлен в плейлист ${reply.playlist.namePl}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun refreshTime(time: String) {
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

    private fun addLikeOrDislike() {
        viewModel.likeOrDislike()
        if (getTrack().isFavorite) {
            binding.ivLike.setImageResource(R.drawable.buttonlike)
        } else {
            binding.ivLike.setImageResource(R.drawable.buttondislike)
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