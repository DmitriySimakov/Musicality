package com.dmitrysimakov.musicality.ui

import androidx.lifecycle.ViewModel
import com.dmitrysimakov.musicality.data.Song
import com.dmitrysimakov.musicality.data.songsCollection
import com.dmitrysimakov.musicality.util.liveData

class MusicChartViewModel : ViewModel() {

    val songs = songsCollection.liveData(Song::class.java)
}