package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchMusicAdapter(private val searchSong: MutableList<Track>, val listener: Listener) : RecyclerView.Adapter <SearchMusicViewHolder> ()
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
        holder.bind(searchSong[position] , listener)

    // Альтернативная реализация слушателя без интерфейса и танцев с бубном
    //        holder.itemView.setOnClickListener {
    //            val onTrackClickListener = OnTrackClickListener()
    //            onTrackClickListener.addClickedTrack(searchSong[position])
    //        }

    }
    interface Listener {
        fun onClickRecyclerItemView (clickedTrack: Track)
    }

}