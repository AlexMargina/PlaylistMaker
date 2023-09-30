package com.example.playlistmaker.search.domain



interface SearchInteractor {
    fun searchTracks(expression: String, consumer: SearchConsumer)
    fun getTracksHistory(consumer: HistoryConsumer)
    fun addTrackToHistory(track: TrackSearchModel)
    fun clearHistory()

    interface SearchConsumer {
        fun consume(tracks: List<TrackSearchModel>?, hasError: Boolean?)
    }

    interface HistoryConsumer {
        fun consume(tracks: List<TrackSearchModel>?)
    }
}