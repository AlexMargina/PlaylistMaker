package com.example.playlistmaker.media.ui.playlist

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LayoutPlaylistsBinding
import com.example.playlistmaker.media.domain.Playlist
import com.example.playlistmaker.utils.Converters

class PlaylistViewHolder (private val binding: LayoutPlaylistsBinding) : RecyclerView.ViewHolder(binding.root)
{

    fun bind(playlist: Playlist, imagePath: String) {
        binding.tvTitle .text = playlist.namePl
        binding.tvCount.text = Converters(itemView.context).convertCountToTextTracks (playlist.countTracks)
        val coverPl = imagePath + "/" + playlist.namePl + ".jpg"
        Log.d ("MAALMI_PlaylistViewHolder", "imagePl = ${ coverPl}")
        Glide.with(itemView)
            .load(coverPl )
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.media_placeholder)
            .centerCrop()
            .transform(RoundedCorners(8))
            .into(binding.ivCover)
    }
}