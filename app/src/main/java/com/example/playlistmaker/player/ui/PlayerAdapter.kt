package com.example.playlistmaker.player.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.LayoutPlayerPlaylistsBinding
import com.example.playlistmaker.media.domain.Playlist

class PlayerAdapter(): RecyclerView.Adapter<PlayerViewHolder>(){
    var playlists = arrayListOf<Playlist>()
    var clickListener: ((Playlist) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)

        return PlayerViewHolder(
            LayoutPlayerPlaylistsBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener?.invoke(playlists[position]) }
    }
}