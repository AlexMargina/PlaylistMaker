package com.example.playlistmaker.presentation


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.Track
import java.text.SimpleDateFormat
import java.util.Locale

class SearchMusicViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView
    private val artistName: TextView
    private val trackTime: TextView
    private val trackImage: ImageView

    init {
        trackName = itemView.findViewById(R.id.songName)
        artistName = itemView.findViewById(R.id.songArtist)
        trackTime = itemView.findViewById(R.id.songTime)
        trackImage = itemView.findViewById(R.id.search_songs)
    }

    fun bind(track: Track, listener : SearchMusicAdapter.Listener) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        val imageUrl = track.artworkUrl100

        Glide.with(trackImage)
            .load(imageUrl)
            .placeholder(R.drawable.media_placeholder)
            .centerCrop()
            .into(trackImage)

        itemView.setOnClickListener {
            listener.onClickRecyclerItemView(track)
        }
    }
}

