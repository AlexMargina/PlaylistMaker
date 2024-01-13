package com.example.playlistmaker.search.data


import android.util.Log
import com.example.playlistmaker.media.data.convertor.TrackDbConvertor
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.search.data.dto.ResponseStatus
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.dto.TracksSearchRequest
import com.example.playlistmaker.search.data.dto.TracksSearchResponse
import com.example.playlistmaker.search.data.network.NetworkClient
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.search.domain.TrackModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.coroutineContext

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchDataStorage: SearchDataStorage,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : SearchRepository {


    override suspend fun searchTrack(expression: String): Flow<ResponseStatus<List<TrackModel>>> =
        flow {
            val response = networkClient.doRequest(TracksSearchRequest(expression))

            when (response.resultCode) {
                - 1 -> {
                    emit(ResponseStatus.Error())
                }

                200 -> {
                    with(response as TracksSearchResponse) {
                        val data = results.map {
                            Log.d ("Maalmi_SearchRepositoryImpl","it.trackTimeMillis1 = ${it.toString()}")
                            if (it.trackTimeMillis !is Long) it.trackTimeMillis = 0L
                            if (it.releaseDate == null ) it.releaseDate = "2001.01.01"
                            if (it.previewUrl ==null ) it.previewUrl = ""
                            if (it.primaryGenreName==null) it.primaryGenreName = ""
                            if (it.collectionName ==null ) it.collectionName = ""
                            if (it.artistName ==null ) it.artistName = ""
                            if (it.country ==null ) it.country = ""
                            if (it.artworkUrl100 ==null ) it.artworkUrl100 = ""
                            TrackModel(
                                trackId = it.trackId,
                                trackName = it.trackName,
                                artistName = it.artistName,
                                trackTimeMillis = it.trackTimeMillis,
                                artworkUrl100 = it.artworkUrl100,
                                collectionName = it.collectionName,
                                releaseDate = it.releaseDate,
                                primaryGenreName = it.primaryGenreName,
                                country = it.country,
                                previewUrl = it.previewUrl,
                                isFavorite = checkIsFavorite(it.trackId)
                            )
                        }

                        emit(ResponseStatus.Success(data))
                    }
                }

                else -> {
                    emit(ResponseStatus.Error())
                }
            }
        }

    suspend fun checkIsFavorite(trackId: String): Boolean {
        return (appDatabase.trackDao().getFavoriteTrack(trackId).size > 0)
    }


    override suspend fun getTrackHistoryList(): List<TrackModel> {
        val historyTracks = searchDataStorage.getSearchHistory().map {
            TrackModel(
                it.trackId,
                it.trackName,
                it.artistName,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.previewUrl,
                it.isFavorite
            )
        }
        clickedTracks.clear()
        clickedTracks.addAll(historyTracks)
        Log.d("MAALMI_SearchRepo","! getTrackHistoryList clickedTracks=${clickedTracks.size}"  )
        return clickedTracks
    }


    override suspend fun addTrackToHistory(track: TrackModel) {
        Log.d("MAALMI_SearchRepo","3. addTrackToHistory track=${track}"  )

        for (clickedTrack in clickedTracks) {
            if (clickedTrack.trackId == track.trackId) {
                clickedTracks.remove(clickedTrack)
                Log.d ("MAALMI_SearchRepo", "4. Удалил запись с trackId= ${track.trackId} ")  //1
                break
            }
        }

        clickedTracks.add(0, track)
        Log.d("MAALMI_SearchRepo", "5. Добавил ${clickedTracks[0].trackId} с isFavorite = ${clickedTracks[0].isFavorite}  " )
        searchDataStorage.addTClickedSearchSongs(
            TrackDto(
                track.trackId,
                track.trackName,
                track.artistName,
                track.trackTimeMillis,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl,
                track.isFavorite
            )
        )
        Log.d("MAALMI_SearchRepo", "6. Отправил на запись в searchDataStorage clickedTracks = ${clickedTracks.size}")
    }

    override suspend fun clearHistory() {
        clickedTracks.clear()
        searchDataStorage.clearHistory()
    }


    companion object {
        var clickedTracks = arrayListOf<TrackModel>()
    }
}