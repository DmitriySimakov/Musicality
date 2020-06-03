package com.dmitrysimakov.musicality.data

data class Song(
    val url: String = "",
    val artist: String = "",
    val title: String = "",
    val album: String = "",
    val art: String = "",
    val duration: String = "",
    val id: String = generateId()
)