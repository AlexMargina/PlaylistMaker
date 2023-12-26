package com.example.playlistmaker.media.ui.displayPlaylist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentDisplayPlaylistBinding
import com.example.playlistmaker.media.ui.playlist.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class DisplayPlaylist : Fragment() {

    private var _binding: FragmentDisplayPlaylistBinding? = null
    private val binding get() = _binding !!
    private val viewModel by viewModel<DisplayPlaylistViewModel>()
    private val adapter = PlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisplayPlaylistBinding.inflate(inflater, container, false)
        return binding.root
        Log.d("MAALMI_PlaylistFragment", "onCreateView = $container")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.liveData.observe(viewLifecycleOwner) { playlistState ->
            Log.d("MAALMI_PlaylistFragment", "playlistState= $playlistState")
        }
    }
}