package com.dmitrysimakov.musicality.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

object BindingAdapters {

    @BindingAdapter("invisibleUnless")
    @JvmStatic fun View.invisibleUnless(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    @BindingAdapter("goneUnless")
    @JvmStatic fun View.goneUnless(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

    @BindingAdapter("imageUrl")
    @JvmStatic fun ImageView.imageUrl(url: String?) {
        val circularProgressDrawable = CircularProgressDrawable(this.context).apply {
            strokeWidth = 4f
            centerRadius = 32f
            start()
        }
        Glide.with(context)
            .load(url)
            .placeholder(circularProgressDrawable)
            .into(this)
    }

    @BindingAdapter("byteArray")
    @JvmStatic fun ImageView.byteArray(byteArray: ByteArray?) {
        val circularProgressDrawable = CircularProgressDrawable(context).apply {
            strokeWidth = 4f
            centerRadius = 32f
            start()
        }
        Glide.with(context)
            .load(byteArray)
            .placeholder(circularProgressDrawable)
            .into(this)
    }
}