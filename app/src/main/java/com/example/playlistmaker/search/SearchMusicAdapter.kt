package com.example.playlistmaker.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.LayoutSearchSongBinding
import com.example.playlistmaker.sharing.domain.Track

class SearchMusicAdapter() : RecyclerView.Adapter <SearchMusicViewHolder> ()
{

    var itemClickListener: ((Track) -> Unit)? = null
    var tracks = mutableListOf<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMusicViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)

        return SearchMusicViewHolder(LayoutSearchSongBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: SearchMusicViewHolder, position: Int) {
        val track = tracks.get(position)
        holder.bind(track)
        holder.itemView.setOnClickListener { itemClickListener?.invoke(tracks[position]) }
    }
}