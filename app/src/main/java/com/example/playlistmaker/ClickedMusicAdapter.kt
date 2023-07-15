package com.example.playlistmaker


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ClickedMusicAdapter (private val clickedSearchSongs: MutableList<Track>, val listener : SearchMusicAdapter.Listener) : RecyclerView.Adapter <SearchMusicViewHolder> ()
{


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMusicViewHolder {
        return SearchMusicViewHolder (LayoutInflater
            .from(parent.context)
            .inflate(R.layout.layout_search_song, parent, false))
    }

    override fun getItemCount(): Int {
        return clickedSearchSongs.size
    }

    override fun onBindViewHolder(holder: SearchMusicViewHolder, position: Int) {
        holder.bind(clickedSearchSongs[position], listener)
    }
}