package com.dmitrysimakov.musicality.data

import androidx.recyclerview.widget.DiffUtil

data class Song(
    val url: String = "",
    val artist: String = "",
    val title: String = "",
    val album: String = "",
    val art: String = "",
    val duration: String = "",
    val id: String = generateId()
)

class SongsDiffCallback : DiffUtil.ItemCallback<Song>() {
    override fun areItemsTheSame(oldItem: Song, newItem: Song) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Song, newItem: Song) = oldItem == newItem
}