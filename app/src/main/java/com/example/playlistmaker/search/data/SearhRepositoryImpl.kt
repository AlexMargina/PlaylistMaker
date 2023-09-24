package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.ITunesResponse
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.network.ITunesSearchApi
import com.example.playlistmaker.search.domain.ResultLoad
import com.example.playlistmaker.search.domain.SearchRepository
import com.example.playlistmaker.sharing.domain.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepositoryImpl(private val api: ITunesSearchApi) : SearchRepository {

    override var tracksLoadResultListener: ResultLoad? = null

    override fun loadTracks(query: String) {

        api.searchSongApi(query)
            .enqueue(object : Callback<ITunesResponse> {
                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>,
                ) {
                    if (response.code() == 200) {
                        val tracks =
                            response.body()?.results!!.map { mapTrack(it) }.filter { track ->
                                track.previewUrl != null
                            }
                        tracksLoadResultListener?.onSuccess(tracks = tracks)

                    }
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    tracksLoadResultListener?.onError()
                }
            })
    }

    private fun mapTrack(trackDto: TrackDto): Track {
        return Track(
            trackDto.trackName,
            trackDto.artistName,
            trackDto.artworkUrl100 ,
            trackDto.trackTimeMillis,
            trackDto.trackId,
            trackDto.collectionName,
            trackDto.releaseDate.orEmpty(),
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl.orEmpty()

        )
    }
}


