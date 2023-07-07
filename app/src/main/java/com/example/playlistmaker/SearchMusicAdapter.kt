package com.example.playlistmaker

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchMusicAdapter(private val searchSong: MutableList<Track>) : RecyclerView.Adapter <SearchMusicViewHolder> ()
{
    var onClickSearchTrack : Track? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMusicViewHolder {
        return SearchMusicViewHolder (LayoutInflater
            .from(parent.context)
            .inflate(R.layout.layout_search_song, parent, false))
    }

    override fun getItemCount(): Int {
        return searchSong.size
    }

    override fun onBindViewHolder(holder: SearchMusicViewHolder, position: Int) {
        holder.bind(searchSong[position])

        holder.itemView.setOnClickListener {
            onClickSearchTrack = searchSong[position]
            Log.d(LOG_TAG, "position: ${position} = ${searchSong[position].trackId}")
            OnTrackClickListener.onTrackClick(onClickSearchTrack!!)
 //          OnTrackSearchedClick.onTrackClick(onClickSearchTrack!!)
        }

    }


}