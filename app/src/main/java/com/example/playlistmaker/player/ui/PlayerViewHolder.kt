package com.example.playlistmaker.player.ui

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LayoutPlayerPlaylistsBinding
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.utils.Converters

class PlayerViewHolder(private val binding: LayoutPlayerPlaylistsBinding) : RecyclerView.ViewHolder(binding.root)
{
    fun bind(playlist: Playlist, imagePath: String) {
        binding.namePl.text = playlist.namePl
        binding.tvCountTracks.text = Converters(itemView.context).convertCountToTextTracks(playlist.countTracks)
        val imagePl = imagePath + "/" + playlist.namePl + ".jpg"
        Glide.with(itemView)
            .load(imagePl)
            .placeholder(R.drawable.media_placeholder)
            .centerCrop()
            .transform(RoundedCorners(3))
            .into(binding.imagePlaylist)
    }
}