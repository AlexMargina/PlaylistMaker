package com.example.playlistmaker.media.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.LayoutPlaylistsBinding
import com.example.playlistmaker.media.domain.Playlist

class PlaylistAdapter : RecyclerView.Adapter<PlaylistViewHolder>() {
    var playlists = arrayListOf<Playlist>()
    var playlistClickListener: ((Playlist) -> Unit)? = null
    var imagePath = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(LayoutPlaylistsBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position], imagePath)
        holder.itemView.setOnClickListener { playlistClickListener?.invoke(playlists[position]) }
    }
}