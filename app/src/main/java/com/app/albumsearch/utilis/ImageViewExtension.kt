package com.app.albumsearch.utilis

import android.widget.ImageView
import com.app.albumsearch.AlbumSearchApplication
import com.app.albumsearch.R


/** extension method for ImageView to load the url image */
fun ImageView.loadFromUrl(url: String?) {
    url?.let {
        val app =context.applicationContext as AlbumSearchApplication
        app.glideRequestManager.load(url)
            .placeholder(R.mipmap.ic_launcher).into(this)
    }
}