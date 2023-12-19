package com.example.playlistmaker.media.ui.playlist

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LayoutPlaylistsBinding
import com.example.playlistmaker.media.domain.Playlist

class PlaylistViewHolder (private val binding: LayoutPlaylistsBinding) : RecyclerView.ViewHolder(binding.root)
{

    fun bind(playlist: Playlist) {
        Glide.with(itemView)
            .load(playlist.imagePl)
            .placeholder(R.drawable.media_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.ivCover)

        binding.tvTitle .text = playlist.namePl
        binding.tvCount.text = convertCountToText(playlist.countTracks)
    }

    private fun convertCountToText(countTracks: Int): String {

        val s = when (countTracks % 10) {
            1 -> "$countTracks трек"
            2, 3, 4 -> "$countTracks трека"
            else -> "$countTracks треков"
        }
        return s
    }
}