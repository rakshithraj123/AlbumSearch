package com.app.albumsearch.repository

import android.content.Context
import com.app.albumsearch.repository.network.RetrofitAPIService
import com.app.albumsearch.repository.network.model.Album
import com.app.albumsearch.repository.network.model.AlbumSearchResponse
import com.app.albumsearch.repository.network.model.CartAlbum
import com.app.albumsearch.repository.storage.StorageUtils

class DataRepository(
    private val context: Context,
    private val retrofitAPIService: RetrofitAPIService
) {
    suspend fun searchAlbums(): AlbumSearchResponse {
        //return retrofitAPIService.searchAlbums("all",artistName,trackName,collectioName,collectionPrice,releasedate)
        return retrofitAPIService.searchAlbums("all")
    }

     fun addAlbumToCart(context: Context, album: Album) {
        StorageUtils.addAlbumToCart(context,album)
    }

     fun removeAlbumFromCart(context: Context, album: Album) {
        StorageUtils.removeAlbumFromCart(context,album)
    }

    fun getCartAlbum(context: Context): CartAlbum {
        return StorageUtils.getCartAlbum(context)
    }

    fun getAlbum(context: Context): AlbumSearchResponse {
        return StorageUtils.getAlbum(context)
    }
}