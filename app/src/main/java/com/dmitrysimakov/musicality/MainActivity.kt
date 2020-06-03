package com.dmitrysimakov.musicality

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dmitrysimakov.musicality.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

private const val RC_CHOOSE_SONG = 1

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding

    private val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.vm = vm
        binding.lifecycleOwner = this
        
        val categories = arrayListOf("Undefined", "Rock", "Rap", "Pop")
        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
//                genre = adapterView?.getItemAtPosition(i).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        
        binding.chooseBtn.setOnClickListener { openAudioFiles() }
        binding.uploadBtn.setOnClickListener { vm.uploadSong() }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == RC_CHOOSE_SONG && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            vm.setSong(uri, getFileName(uri, contentResolver))
        }
    }
    
    private fun openAudioFiles() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        startActivityForResult(intent, RC_CHOOSE_SONG)
    }
}