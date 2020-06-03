package com.dmitrysimakov.musicality.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.dmitrysimakov.musicality.data.Song
import com.dmitrysimakov.musicality.data.SongsDiffCallback
import com.dmitrysimakov.musicality.databinding.ItemSongBinding
import com.dmitrysimakov.musicality.util.DataBoundListAdapter

class SongsAdapter(
    private val playClick: ((String) -> Unit),
    clickCallback: ((Song) -> Unit)
) : DataBoundListAdapter<Song, ItemSongBinding>(clickCallback, SongsDiffCallback()) {

    override fun createBinding(parent: ViewGroup): ItemSongBinding = ItemSongBinding
        .inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemSongBinding, item: Song) {
        binding.song = item
        binding.playBtn.setOnClickListener { playClick(item.url) }
    }
}