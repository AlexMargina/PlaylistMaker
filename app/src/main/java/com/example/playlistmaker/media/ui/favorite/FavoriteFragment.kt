package com.example.playlistmaker.media.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.search.domain.TrackModel
import com.example.playlistmaker.search.ui.SearchFragment
import com.example.playlistmaker.search.ui.SearchMusicAdapter
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel by viewModel<FavoriteViewModel>()
    private val favoriteSong = ArrayList<TrackModel>()
    private val favoriteMusicAdapter = SearchMusicAdapter(favoriteSong) { trackClickListener(it) }
    private lateinit var trackClickListener: (TrackModel) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
           state -> updateFavorite(state)
        }

        binding.recyclerViewFavorited.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorited.adapter = favoriteMusicAdapter


        trackClickListener = debounce(
            SearchFragment.CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false
        ) { track ->
            viewModel.setClickedTrack(track, this)
            runPlayer(track.trackId.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    private fun runPlayer(trackId: String) {
        Log.d("MAALMI_FavTrag", "runPlayer($trackId) ")
        findNavController().navigate(R.id.playerFragment)
    }

    private fun updateFavorite(state: FavoriteState) {
        binding.apply {

            when (state) {
                is FavoriteState.Content -> {
                    Log.d("MAALMI_FavFrag", "Выполняем Content ")
                    binding.ivEmptyFavorite.isVisible=false
                    binding.tvEmptyFavorite.isVisible=false
                    binding.groupFavorited.isVisible =true

                    Log.d("MAALMI_FavTrag", "favoriteSong = $favoriteSong ")
                    favoriteMusicAdapter.tracks.clear()
                    state.tracks.map { trackModel -> trackModel.isFavorite=true }
                    favoriteMusicAdapter.tracks.addAll(state.tracks)
                    favoriteMusicAdapter.notifyDataSetChanged()
                }
                is FavoriteState.Empty -> {
                    Log.d("MAALMI_FavFrag", "Выполняем Empty")
                    binding.ivEmptyFavorite.isVisible=true
                    binding.groupFavorited.isVisible = false
                }
                else -> {

                }
            }
        }
    }

    companion object {
        fun newInstance() = FavoriteFragment().apply {
            arguments = Bundle().apply {}
        }
    }
}