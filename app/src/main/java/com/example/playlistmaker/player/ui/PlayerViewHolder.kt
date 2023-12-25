package com.example.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LayoutPlayerPlaylistsBinding
import com.example.playlistmaker.media.domain.Playlist

class PlayerViewHolder(private val binding: LayoutPlayerPlaylistsBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(playlist: Playlist) {
        binding.namePl.text = playlist.namePl
        binding.tvCountTracks.text = convertCountToText(playlist.countTracks)
        Glide.with(itemView)
            .load(playlist.imagePl)
            .placeholder(R.drawable.media_placeholder)
            .centerCrop()
            .transform(RoundedCorners(3))
            .into(binding.imagePlaylist)
//
//    itemView.setOnClickListener { PlayerAdapter().clickListener?.invoke(playlist) }
//        Log.d("MAALMI_MusicAdapter", "playlist=${playlist}")
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