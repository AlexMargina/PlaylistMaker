package com.example.playlistmaker.media.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.newPlaylist.NewPlaylistRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class NewPlaylistRepositoryImpl(val context: Context) : NewPlaylistRepository {

    private val filePath =
        File(context.getDir("Covers", Context.MODE_APPEND), R.string.app_name.toString())
    private val filePath2 = File("/data/data",  R.string.app_name.toString())
    private val filePath1 = (context.filesDir)

    init {
        if (! filePath1.exists()) filePath1.mkdirs()
        if (! filePath.exists()) filePath.mkdirs()
    }

    override fun imagePath(): String = filePath.path

    override suspend fun savePicture(uri: Uri, fileName: String) {
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "$fileName.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = context.contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }
        // записываем картинку с помощью BitmapFactory
        Log.d("MAALMI_savePicture", "uri= $uri, fileName=$fileName ")
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }

    override suspend fun deletePicture(oldNamePl: String) {
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "$oldNamePl.jpg")
        if (file.exists()) {
            file.delete()
        }
        val cacheFile = File(context.cacheDir, "$oldNamePl.jpg")
        if (cacheFile.exists()) {
            cacheFile.delete()
        }
        Log.d("MAALMI_deletePicture", "Удален ${file.toString()} ")
    }

    override suspend fun loadPicture(imageFileName: String): Uri? {
        val file = File(filePath, "$imageFileName.jpg")
        Log.d("MAALMI_loadPicture", "load... uri= ${file.toUri()}, fileName=$imageFileName ")
        return if (file.exists()) {
            file.toUri()
        } else {
            null
        }
    }
}