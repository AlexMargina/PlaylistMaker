package com.example.playlistmaker.media.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {

    private var _binding: FragmentMediaBinding? = null
    private val binding get()=_binding!!
    private var _tabMediator: TabLayoutMediator? = null
    private val tabMediator get() = _tabMediator !!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentMediaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)
        _tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.setText(R.string.fav_tracks)
                1 -> tab.setText(R.string.playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _tabMediator?.detach()
    }
}