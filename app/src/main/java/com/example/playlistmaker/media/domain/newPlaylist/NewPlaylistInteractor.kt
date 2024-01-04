package com.example.playlistmaker.media.domain.newPlaylist

import android.net.Uri

interface NewPlaylistInteractor {

    suspend fun savePicture(uri: Uri, fileName: String)
    suspend fun loadPicture(fileName: String): Uri?
    fun imagePath () : String

    suspend fun deletePicture(oldNamePl: String)
}