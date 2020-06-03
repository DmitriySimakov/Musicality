package com.dmitrysimakov.musicality.ui

import android.app.Application
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.*
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dmitrysimakov.musicality.data.*
import com.dmitrysimakov.musicality.util.Event
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddSongViewModel(app: Application) : AndroidViewModel(app) {

    private val songUri = MutableLiveData<Uri?>()

    private val metadataRetriever = MediaMetadataRetriever()

    val filename = MutableLiveData("")
    val artist = MutableLiveData("")
    val title = MutableLiveData("")
    val album = MutableLiveData("")
    val duration = MutableLiveData("")
    val art = MutableLiveData<ByteArray>()

    val uploading = MutableLiveData(false)
    val uploadedEvent = MutableLiveData<Event<Unit>>()

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
        val artist = artist.value ?: return@launch
        val title = title.value ?: return@launch
        val album = album.value ?: ""
        val art = art.value
        val duration = duration.value ?: ""

        uploading.value = true

        val songId = generateId()
        val songRef = songsStorage.child(songId)
        songRef.putFile(songUri).await()
        val songUrl = songRef.downloadUrl.await().toString()
        var artUrl = ""
        if (art != null) {
            val artRef = artsStorage.child(songId)
            artRef.putBytes(art).await()
            artUrl = artRef.downloadUrl.await().toString()
        }

        val song = Song(songId, songUrl, artist, title, album, artUrl, duration)
        songsCollection.document(song.id).set(song).await()
        uploadedEvent.value = Event(Unit)
    }
}