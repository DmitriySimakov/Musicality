package com.dmitrysimakov.musicality.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(directions: NavDirections) = findNavController().navigate(directions)

fun Fragment.popBackStack() = findNavController().popBackStack()

@SuppressLint("Recycle")
fun getFileName(uri: Uri, contentResolver: ContentResolver) : String? {
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