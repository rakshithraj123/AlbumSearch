package com.app.albumsearch.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.albumsearch.repository.network.model.AlbumSearchResponse
import com.app.albumsearch.repository.DataRepository
import com.app.albumsearch.repository.storage.StorageUtils
import com.app.albumsearch.repository.storage.StorageUtils.getAlbum
import kotlinx.coroutines.Dispatchers

class MainViewModel(
    private val applicationContext: Context,
    private val dataRepository: DataRepository
) : ViewModel() {

    fun fetchAlbums() = liveData(Dispatchers.IO) {
        try {

            val response = dataRepository.searchAlbums()
            emit(response)

        } catch (exception: Exception) {
            Log.d("Exception", exception.toString())
            emit(AlbumSearchResponse(0, mutableListOf()))
        }
    }

    fun getAlbum(): AlbumSearchResponse {
        return dataRepository.getAlbum(applicationContext)
    }
}