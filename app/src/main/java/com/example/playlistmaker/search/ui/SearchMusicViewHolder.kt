package com.example.playlistmaker.search.ui


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LayoutSearchSongBinding
import com.example.playlistmaker.search.domain.TrackModel


class SearchMusicViewHolder(private val binding: LayoutSearchSongBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: TrackModel) {

        binding.songName.text = model.trackName
        binding.songArtist.text = model.artistName
        binding.songTime.text = model.getDuration()


        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.media_placeholder)
            .centerCrop()
            .into(binding.searchSongs)
    }
}