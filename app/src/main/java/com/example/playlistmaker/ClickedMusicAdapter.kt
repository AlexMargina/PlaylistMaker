package com.example.playlistmaker


import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ClickedMusicAdapter (private val clickedSearchSongs: MutableList<Track>) : RecyclerView.Adapter <SearchMusicViewHolder> ()
{
    var onClickSearchTrack : Track? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMusicViewHolder {
        return SearchMusicViewHolder (LayoutInflater
            .from(parent.context)
            .inflate(R.layout.layout_search_song, parent, false))
    }

    override fun getItemCount(): Int {
        return clickedSearchSongs.size
    }

    override fun onBindViewHolder(holder: SearchMusicViewHolder, position: Int) {
        holder.bind(clickedSearchSongs[position])

        holder.itemView.setOnClickListener {
            onClickSearchTrack = clickedSearchSongs[position]
            Log.d(LOG_TAG, "position: ${position} = ${clickedSearchSongs[position].trackId}")


        }

    }
}