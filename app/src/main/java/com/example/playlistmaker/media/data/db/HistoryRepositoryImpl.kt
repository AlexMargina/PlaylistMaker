package com.example.playlistmaker.media.data.db

import com.example.playlistmaker.media.data.db.convertor.TrackDbConvertor
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.db.HistoryRepository
import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HistoryRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : HistoryRepository {
    override fun historyTracks(): Flow<List<TrackModel>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromMovieEntity(tracks))
    }

    private fun convertFromMovieEntity(tracks: List<TrackEntity>): List<TrackModel> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}