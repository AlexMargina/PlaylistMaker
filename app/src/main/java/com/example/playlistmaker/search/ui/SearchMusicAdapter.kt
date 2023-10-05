package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.LayoutSearchSongBinding
import com.example.playlistmaker.search.domain.TrackModel

class SearchMusicAdapter(
    private val tracks: ArrayList<TrackModel>,
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<SearchMusicViewHolder>() {

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
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks.get(position)) }
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: TrackModel)
    }
}