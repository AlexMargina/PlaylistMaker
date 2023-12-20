package com.example.playlistmaker.media.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.get
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.media.ui.newPlaylist.NewPlaylistViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.liveData.observe(viewLifecycleOwner)  { playlistState ->
            when (playlistState) {
                PlaylistState.Empty -> showEmptyResult()
                is PlaylistState.Playlists -> showPlaylists(playlistState.playlists)
            }
        }

        newViewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            adapter.playlists.clear()
            adapter.playlists.addAll(playlist)
            adapter.notifyDataSetChanged()
        }

        binding.btNewPlaylist.setOnClickListener {
            val navigation = findNavController()
            navigation.navigate(R.id.action_mediaFragment_to_newPlaylistFragment)
            navigation.addOnDestinationChangedListener {_, nd, _ ->
                if(nd.id == R.id.action_mediaFragment_to_newPlaylistFragment){
                    //nd.
                }else{
                    //navigation.visibility = View.VISIBLE
                }
            }
        }

        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.adapter = adapter
    }

    private fun showPlaylists(playlists: List<Playlist>) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.ivEmptyPlaylist.visibility = View.GONE
        binding.tvEmptyPlaylist.visibility = View.GONE
        adapter.playlists.clear()
        adapter.playlists.addAll(playlists)
        adapter.notifyDataSetChanged()
    }

    private fun showEmptyResult() {
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
