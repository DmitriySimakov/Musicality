package com.dmitrysimakov.musicality

import android.app.Application
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.*
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dmitrysimakov.musicality.data.Song
import com.dmitrysimakov.musicality.data.generateId
import com.dmitrysimakov.musicality.data.songsStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val songUri = MutableLiveData<Uri?>()

    private val metadataRetriever = MediaMetadataRetriever()

    val filename = MutableLiveData("")
    val artist = MutableLiveData("")
    val title = MutableLiveData("")
    val album = MutableLiveData("")
    val duration = MutableLiveData("")
    val art = MutableLiveData<ByteArray>()

    fun setSong(uri: Uri, filename: String?) {
        songUri.value = uri
        this.filename.value = filename
        metadataRetriever.setDataSource(getApplication(), uri)
        extractMetadata()
    }

    private fun extractMetadata() {
        artist.value = metadataRetriever.extractMetadata(METADATA_KEY_ARTIST)
        title.value = metadataRetriever.extractMetadata(METADATA_KEY_TITLE)
        album.value = metadataRetriever.extractMetadata(METADATA_KEY_ALBUM)
        duration.value = metadataRetriever.extractMetadata(METADATA_KEY_DURATION)
        art.value = metadataRetriever.embeddedPicture
    }

    fun uploadSong() = viewModelScope.launch {
        val songUri = songUri.value ?: return@launch
        val filename = filename.value ?: return@launch

        val fileExtension = filename.substring(filename.indexOf('.'))
        val storageFilename = generateId() + fileExtension
        val songRef = songsStorage.child(storageFilename)
        songRef.putFile(songUri).await()
        val downloadUrl = songRef.downloadUrl.await().toString()
        val song = Song()
    }
}