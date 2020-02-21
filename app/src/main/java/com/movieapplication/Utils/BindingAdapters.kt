package com.movieapplication.Utils

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("adapter")
fun setAdapter(view: androidx.recyclerview.widget.RecyclerView, adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>) {
    view.setHasFixedSize(true)
    view.adapter = adapter
}


@BindingAdapter("imageUrl", "error")
fun loadImage(view: ImageView, url: String, error: Drawable) {
    Picasso.get().load(url).error(error).into(view)
}

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, imageUrl: String?) {
    if (imageUrl != null) {
        Picasso.get().load(imageUrl).into(imageView)
    }
}

@BindingAdapter("font")
fun setTypeface(view: TextView, font: String) {
    view.setTypeface(Typeface.createFromAsset(view.context.assets, "fonts/" + font + ".ttf"))
}

