package com.example.playlistmaker.media.ui.playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.media.ui.displayPlaylist.DisplayPlaylistFragment
import com.example.playlistmaker.media.ui.newPlaylist.NewPlaylistViewModel
import com.example.playlistmaker.media.ui.playlist.PlaylistState.Playlists
import com.example.playlistmaker.search.ui.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding  get() = _binding!!
    private val viewModel by viewModel<PlaylistViewModel>()
    private val newViewModel by viewModel<NewPlaylistViewModel>()
    private val adapter = PlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
        Log.d ("MAALMI_PlaylistFragment", "onCreateView = $container")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d ("MAALMI_PlaylistFragment", "onViewCreated = $view")
        viewModel.getPlaylist()
        viewModel.liveData.observe(viewLifecycleOwner)  { playlistState ->
            Log.d ("MAALMI_PlaylistFragment", "playlistState= $playlistState")
            when (playlistState) {
                is PlaylistState.Empty -> showEmptyResult()
                is Playlists -> showPlaylists(playlistState.playlists)
            }
        }

        newViewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            adapter.imagePath = newViewModel.imagePath()
            adapter.playlists.clear()
            adapter.playlists.addAll(playlist)
            adapter.notifyDataSetChanged()
        }

        binding.btNewPlaylist.setOnClickListener {
            val navigation = findNavController()
            navigation.navigate(R.id.newPlaylistFragment)
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.adapter = adapter

        adapter.playlistClickListener = debounce<Playlist>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playlist ->
            displayPlaylist(playlist)
        }

 } //=================================================================================

    override fun onResume() {
        super.onResume()
        newViewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            adapter.imagePath = newViewModel.imagePath()
            adapter.playlists.clear()
            adapter.playlists.addAll(playlist)
            adapter.notifyDataSetChanged()
        }
    }

    private fun onPlaylistClickDebounce(playlist: Playlist) = debounce<Playlist>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { playlist ->
            displayPlaylist(playlist)
        }



    private fun displayPlaylist(playlist: Playlist) {
        findNavController().navigate(
            R.id.displayPlaylist,
            DisplayPlaylistFragment.passArgs (playlist.idPl)
        )
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        Log.d ("MAALMI_PlaylistFragment", "showPlaylists = $playlists")
        binding.recyclerView.visibility = View.VISIBLE
        binding.ivEmptyPlaylist.visibility = View.GONE
        binding.tvEmptyPlaylist.visibility = View.GONE
        adapter.imagePath = newViewModel.imagePath()
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    private fun showEmptyResult() {
        Log.d ("MAALMI_PlaylistFragment", "showEmptyResult ")
        binding.recyclerView.visibility = View.GONE
        binding.ivEmptyPlaylist.visibility = View.VISIBLE
        binding.tvEmptyPlaylist.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = PlaylistFragment()
    }
}