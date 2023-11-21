package com.example.playlistmaker.media.data.db

import com.example.playlistmaker.media.data.db.convertor.TrackDbConvertor
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.db.FavoriteRepository
import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteRepository {

    override fun favoriteTracks(): Flow<List<TrackModel>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromMovieEntity(tracks))
    }

    override suspend fun deleteDbTrack(trackId: String) {
        appDatabase.trackDao().deleteTrack(trackId)
    }

    override suspend fun insertDbTrack(track: TrackModel) {
       // appDatabase.trackDao().insertFavoriteTrack(track)
    }

    private fun convertFromMovieEntity(tracks: List<TrackEntity>): List<TrackModel> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}