package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchMusicAdapter (private val searchSong : ArrayList<Track>) : RecyclerView.Adapter <SearchMusicViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMusicViewHolder {
        val searchMusic = LayoutInflater.from(parent.context).inflate(R.layout.layout_search_song, parent, false)
        return SearchMusicViewHolder (searchMusic)
    }

    override fun getItemCount() =  searchSong.size

    override fun onBindViewHolder(holder: SearchMusicViewHolder, position: Int) {
        holder.bind(searchSong[position])
    }

}