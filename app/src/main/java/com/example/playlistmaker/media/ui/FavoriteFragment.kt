package com.example.playlistmaker.media.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.player.ui.PlayerActivity
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
        binding.ivEmptyFavorite.setImageResource(R.drawable.song_not_found)
        binding.tvEmptyFavorite.text = getString(R.string.empty_favorites)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.stateLiveData.observe(viewLifecycleOwner) {
           it -> updateFavorite(it)
            Log.d("MAALMI", "Изменение liveData ${it.toString()}")
        }

        binding.recyclerViewFavorited.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorited.adapter = favoriteMusicAdapter

        trackClickListener = debounce(
            SearchFragment.CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false
        ) { track ->
 //           viewModel.addTrackToHistory(track, this)
            runPlayer(track.trackId.toString())
        }
    }


    private fun runPlayer(trackId: String) {
        val playerIntent = Intent(requireContext(), PlayerActivity::class.java)
        playerIntent.putExtra("trackId", trackId)
        startActivity(playerIntent)
    }


    private fun updateFavorite(state: FavoriteState) {
        binding.apply {


            when (state) {
                is FavoriteState.Content -> {
                    Log.d("MAALMI", "Выполняем Content ")
                    binding.ivEmptyFavorite.isVisible=false
                    binding.groupFavorited.isVisible =true
                    favoriteSong.clear()
                    favoriteSong.addAll(state.tracks )
                    Log.d("MAALMI", "avoriteSong = $favoriteSong ")
                    favoriteMusicAdapter.notifyDataSetChanged()
                }

                else -> {
                    Log.d("MAALMI", "Выполняем Empty")
                    binding.ivEmptyFavorite.isVisible=true
                    binding.groupFavorited.isVisible = false
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
