package com.app.albumsearch.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.app.albumsearch.repository.network.model.Album
import com.app.albumsearch.repository.network.model.AlbumSearchResponse
import com.app.albumsearch.repository.DataRepository

class StoryViewModel(
    private val applicationContext: Context,
    private val dataRepository: DataRepository
) : ViewModel() {
    lateinit var albumSearchResponse  : AlbumSearchResponse

    internal fun addAlbumToCart(context: Context, album: Album) {
        dataRepository.addAlbumToCart(context,album)
    }

    internal fun removeAlbumFromCart(context: Context, album: Album) {
        dataRepository.removeAlbumFromCart(context,album)
    }

}