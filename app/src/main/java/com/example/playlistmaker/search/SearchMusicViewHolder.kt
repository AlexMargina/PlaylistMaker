package com.example.playlistmaker.search


import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.LayoutSearchSongBinding
import com.example.playlistmaker.sharing.domain.Track
import java.text.SimpleDateFormat
import java.util.Locale

class SearchMusicViewHolder (private val binding: LayoutSearchSongBinding)
    : RecyclerView.ViewHolder(binding.root) {


    fun bind(track: Track) {
        binding.songName.text = track.trackName
        binding.songArtist.text = track.artistName.toString()
        binding.songTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        val imageUrl = track.artworkUrl100
       Glide.with(binding.searchSongs)
            .load(imageUrl)
            .placeholder(R.drawable.media_placeholder)
            .centerCrop()
            .into(binding.searchSongs)

//        itemView.setOnClickListener {
//            listener.onClickRecyclerItemView(track)
//        }
    }
}

