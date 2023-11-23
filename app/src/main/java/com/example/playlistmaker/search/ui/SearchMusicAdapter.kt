package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.LayoutSearchSongBinding
import com.example.playlistmaker.search.domain.TrackModel

class SearchMusicAdapter(
    tracksForRecycler: ArrayList<TrackModel>
) : RecyclerView.Adapter<SearchMusicViewHolder>() {

    var tracks = tracksForRecycler
    var itemСlick : ((TrackModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMusicViewHolder {
        return SearchMusicViewHolder(
            LayoutSearchSongBinding
                .inflate(
                    LayoutInflater
                    .from(parent.context), parent, false
                )
        )
    }

    override fun getItemCount() = tracks.size

    override fun onBindViewHolder(holder: SearchMusicViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            itemСlick?.invoke(tracks[position])
        }
    }
}