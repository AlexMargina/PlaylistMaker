package com.example.playlistmaker.media.data.db

import com.example.playlistmaker.media.data.db.convertor.TrackDbConvertor
import com.example.playlistmaker.media.data.db.entity.TrackEntity
import com.example.playlistmaker.media.domain.db.FavoriteRepository
import com.example.playlistmaker.search.data.SearchRepositoryImpl.Companion.clickedTracks
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavoriteRepository {

    override fun favoriteTracks(): Flow<ArrayList<TrackModel>> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks as ArrayList<TrackEntity>))
    }

    override suspend fun deleteDbTrack(trackId: String) {
        appDatabase.trackDao().deleteTrack(trackId)
    }


    private fun convertFromTrackEntity(tracks: ArrayList<TrackEntity>): ArrayList<TrackModel> {
        return tracks.map { track -> trackDbConvertor.map(track) } as ArrayList<TrackModel>
    }

    override fun setClickedTrack(track: TrackModel) {
        clickedTracks.add(0,track)
    }

    private suspend fun saveTracks(tracks: List<TrackDto>) {
        val trackEntities = tracks.map { track -> trackDbConvertor.map(track) }
        appDatabase.trackDao().insertTracks(trackEntities)
    }


}