package com.dmitrysimakov.musicality

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dmitrysimakov.musicality.data.songsStorage
import com.dmitrysimakov.musicality.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val RC_CHOOSE_SONG = 1

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    private val metadataRetriever = MediaMetadataRetriever()
    
    private var genre = "Undefined"
    private var songUri: Uri? = null
    private var art: ByteArray? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        
        val categories = arrayListOf("Undefined", "Rock", "Rap", "Pop")
        val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long) {
                genre = adapterView?.getItemAtPosition(i).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
        
        binding.chooseBtn.setOnClickListener { openAudioFiles() }
        binding.uploadBtn.setOnClickListener { uploadSong() }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == RC_CHOOSE_SONG && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            songUri = uri
            binding.filename.text = getFileName(uri)
            metadataRetriever.setDataSource(this, songUri)
            binding.artist.text = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
            binding.title.text = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            binding.album.text = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
            binding.duration.text = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            
            art = metadataRetriever.embeddedPicture?.also { art ->
                binding.art.setImageBitmap(BitmapFactory.decodeByteArray(art, 0, art.size))
            }
        }
    }
    
    private fun openAudioFiles() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        startActivityForResult(intent, RC_CHOOSE_SONG)
    }
    
    @SuppressLint("Recycle")
    private fun getFileName(uri: Uri) : String? {
        if (uri.scheme.equals("content")) {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor.use { c ->
                if (c?.moveToFirst() == true) {
                    return c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        
        return uri.path?.substringAfter('/')
    }
    
    private fun getFileExtension(uri: Uri) =
            MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri))
    
    private fun uploadSong() {
        val songUri = songUri
        if (songUri == null) {
            Toast.makeText(this, "NOPE", Toast.LENGTH_SHORT).show()
            return
        }
        
        binding.progressbar.visibility = View.VISIBLE
        GlobalScope.launch {
            val snapshot= songsStorage.child("${System.currentTimeMillis()}.${getFileExtension(songUri)}")
                    .putFile(songUri).await()
            
        }
    }
}