package com.app.albumsearch.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.app.albumsearch.repository.network.model.CartAlbum
import com.app.albumsearch.repository.DataRepository
import kotlinx.coroutines.Dispatchers

class CartViewModel (
    private val applicationContext: Context,
    private val dataRepository: DataRepository
) : ViewModel() {

    fun getCartAblum() = liveData(Dispatchers.IO) {
        try {

            val response = dataRepository.getCartAlbum(applicationContext)
            emit(response)

        } catch (exception: Exception) {
            Log.d("Exception",exception.toString())
            emit(CartAlbum())
        }
    }
}