package com.example.playlistmaker.media.ui.displayPlaylist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentDisplayPlaylistBinding
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.search.domain.TrackModel
import com.example.playlistmaker.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.search.ui.SearchMusicAdapter
import com.example.playlistmaker.utils.debounce
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class DisplayPlaylistFragment : Fragment() {

    private var _binding: FragmentDisplayPlaylistBinding? = null
    private val binding get() = _binding !!
    private val viewModel by viewModel<DisplayPlaylistViewModel>()
    private lateinit var trackClickListener: (TrackModel) -> Unit
    private val adapter = SearchMusicAdapter(arrayListOf<TrackModel>(), {trackClickListener(it)})


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisplayPlaylistBinding.inflate(inflater, container, false)
        return binding.root
        Log.d("MAALMI_DisplayPlaylistFragment", "playlistId = ${Bundle.CONTENTS_FILE_DESCRIPTOR}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idPl = requireArguments().getInt("PLAYLIST")
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        viewModel.getPlaylistById(idPl)

        viewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            adapter.tracks.clear()
            adapter.tracks.addAll(playlist.tracksPl)
            adapter.notifyDataSetChanged()
            displayPlaylist(playlist)
            Log.d("MAALMI_DisplayPlaylistFragment", "playlist= $playlist")
        }

        binding.recyclerTracksOfPlaylist.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTracksOfPlaylist.adapter = adapter

        trackClickListener = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            Log.d("MAALMI_SearchFrag", "1. Нажали на track=${track}")
            viewModel.addTrackToHistory(track)
            runPlayer(track.trackId.toString())
        }
    }
    private fun displayTracks (tracks : List<TrackModel>) {
        adapter.tracks.clear()
        adapter.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()
    }

    private fun displayPlaylist(playlist:Playlist) {
        binding.apply {
            tvNamePl.text = playlist.namePl
            tvDesciptPl.text = playlist.descriptPl
            tvPlaylistCount.text = playlist.countTracks.toString()
            tvPlaylistTime.text = (playlistTime (playlist)).toString()
        }
    }

    private fun runPlayer(trackId: String) {
        findNavController().navigate(R.id.playerFragment)
    }

    private fun playlistTime (playlist: Playlist) : Long {
        var result = 0L
        for (track in playlist.tracksPl) {
            result += track.trackTimeMillis
        }
        return result
    }


    companion object {
        fun passArgs(playlistId: Int): Bundle = bundleOf("PLAYLIST" to playlistId)
    }
}