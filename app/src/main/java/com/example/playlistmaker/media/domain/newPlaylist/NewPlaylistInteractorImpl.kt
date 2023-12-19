package com.example.playlistmaker.media.domain.newPlaylist

import android.net.Uri

class NewPlaylistInteractorImpl(private val repository: NewPlaylistRepository) :
    NewPlaylistInteractor {

    override suspend fun savePicture(uri: Uri, fileName: String) {
        repository.savePicture(uri, fileName)
    }

    override suspend fun loadPicture(fileName: String): Uri? {
        return repository.loadPicture(fileName)
    }
}