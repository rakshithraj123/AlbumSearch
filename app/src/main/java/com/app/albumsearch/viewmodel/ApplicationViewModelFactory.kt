package com.app.albumsearch.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import com.app.albumsearch.repository.DataRepository

/**
 * View Model Factory class, which is used to produce the view models
 */
class ApplicationViewModelFactory(
    private val applicationContext: Context,
    private val dataRepo: DataRepository
) : Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                return MainViewModel(applicationContext, dataRepo) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                return StoryViewModel(applicationContext, dataRepo) as T
            }
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                return CartViewModel(applicationContext, dataRepo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }


    }
}