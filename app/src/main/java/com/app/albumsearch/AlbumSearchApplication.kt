package com.app.albumsearch

import android.app.Application
import com.app.albumsearch.repository.DataRepository
import com.app.albumsearch.viewmodel.ApplicationViewModelFactory
import com.app.albumsearch.repository.network.RetrofitInstance
import com.bumptech.glide.Glide
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class AlbumSearchApplication : Application(), KodeinAware {
    //Glide RequestManager to load image
    val glideRequestManager  by lazy {
        Glide.with(applicationContext)
    }


    override val kodein: Kodein = Kodein {

        bind<DataRepository>() with singleton {
            DataRepository(applicationContext, RetrofitInstance.retrofitAPIService)
        }

        bind<ApplicationViewModelFactory>() with singleton {
            ApplicationViewModelFactory(applicationContext, instance())
        }
    }

}