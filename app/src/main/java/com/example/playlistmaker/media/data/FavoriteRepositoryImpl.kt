package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.convertor.TrackDbConvertor
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.entity.TrackEntity
import com.example.playlistmaker.media.domain.favorite.FavoriteRepository
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
        val tracks = appDatabase.trackDao().getFavoriteTracksByTime()
        emit(convertFromTrackEntity(tracks as ArrayList<TrackEntity>))
    }

    override suspend fun deleteDbTrack(trackId: String) {
        appDatabase.trackDao().deleteTrack(trackId)
    }


    private fun convertFromTrackEntity(tracks: ArrayList<TrackEntity>): ArrayList<TrackModel> {
        return tracks.map { track -> trackDbConvertor.map(track) } as ArrayList<TrackModel>
    }

    override fun setClickedTrack(track: TrackModel) {
        clickedTracks.add(0, track)
    }

    private suspend fun saveTracks(tracks: List<TrackDto>) {
        val trackEntities = tracks.map { track -> trackDbConvertor.map(track) }
        appDatabase.trackDao().insertTracks(trackEntities)
    }

    override suspend fun insertDbTrackToFavorite(track: TrackModel) {
        track.isFavorite = true
        val listTracks = arrayListOf<TrackModel>()
        listTracks.add(track)
        val trackEntity = convertToTrackEntity(listTracks)
        appDatabase.trackDao().insertFavoriteTrack(trackEntity)
        clickedTracks.add(0, track)
    }

    private fun convertToTrackEntity(listTracks: ArrayList<TrackModel>): ArrayList<TrackEntity> {
        return listTracks.map { track -> trackDbConvertor.map(track) } as ArrayList<TrackEntity>
    }

    override suspend fun deleteDbTrackFromFavorite(trackId: String) {

        val trackDislikeOnPosition =
            clickedTracks.filter { trackModel -> trackModel.trackId == trackId }[0]
        val position = clickedTracks.indexOf(trackDislikeOnPosition)
        clickedTracks[position].isFavorite = false
        appDatabase.trackDao().deleteTrackFromFavorite(trackId)
        appDatabase.linkTrackPlDao().deleteOrfanTrack()
    }
}