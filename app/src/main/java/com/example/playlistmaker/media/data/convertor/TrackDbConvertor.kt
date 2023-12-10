package com.example.playlistmaker.media.data.convertor

import com.example.playlistmaker.media.data.entity.TrackEntity
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.TrackModel

class TrackDbConvertor {

    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
            )
    }

    fun map(track: TrackEntity): TrackModel {
        return TrackModel(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }


    fun map(track: TrackModel): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}

