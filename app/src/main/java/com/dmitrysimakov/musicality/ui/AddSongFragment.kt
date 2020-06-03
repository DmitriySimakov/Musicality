package com.dmitrysimakov.musicality.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.dmitrysimakov.musicality.databinding.FragmentAddSongBinding
import com.dmitrysimakov.musicality.util.getFileName
import com.dmitrysimakov.musicality.util.popBackStack

private const val RC_CHOOSE_SONG = 1

class AddSongFragment : Fragment() {

    private lateinit var binding: FragmentAddSongBinding

    private val vm by viewModels<AddSongViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddSongBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = vm

        val categories = arrayListOf("Undefined", "Rock", "Rap", "Pop")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_expandable_list_item_1, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
//                genre = adapterView?.getItemAtPosition(i).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.chooseBtn.setOnClickListener { openAudioFiles() }
        binding.uploadBtn.setOnClickListener { vm.uploadSong() }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vm.uploadedEvent.observe(viewLifecycleOwner) { popBackStack() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_CHOOSE_SONG && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            val filename = getFileName(uri, requireActivity().contentResolver)
            vm.setSong(uri, filename)
        }
    }

    private fun openAudioFiles() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        startActivityForResult(intent, RC_CHOOSE_SONG)
    }
}