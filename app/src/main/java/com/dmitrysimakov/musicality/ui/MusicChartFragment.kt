package com.dmitrysimakov.musicality.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.dmitrysimakov.musicality.R
import com.dmitrysimakov.musicality.ui.MusicChartFragmentDirections.Companion.toAddSongFragment
import com.dmitrysimakov.musicality.util.navigate
import kotlinx.android.synthetic.main.fragment_music_chart.*

class MusicChartFragment : Fragment() {

    private val vm by viewModels<MusicChartViewModel>()

    private val adapter by lazy { SongsAdapter(
        {  },
        {  }
    )}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_music_chart, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.adapter = adapter

        vm.songs.observe(viewLifecycleOwner) { adapter.submitList(it) }

        fab.setOnClickListener { navigate(toAddSongFragment()) }
    }

}