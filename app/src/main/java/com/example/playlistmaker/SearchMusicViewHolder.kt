package com.example.playlistmaker


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

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

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime
        val imageUrl = track.artworkUrl100
        Glide.with(trackImage)
            .load(imageUrl)
            .placeholder(R.drawable.media)
            .transform(RoundedCorners(10))
            .centerCrop()
            .into(trackImage)
    }
}

