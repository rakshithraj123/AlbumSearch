package com.app.albumsearch.ui

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.albumsearch.repository.DataRepository
import com.app.albumsearch.viewmodel.ApplicationViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

/**
 * Base activity, from which all the activities in the app are extended.
 * This is a Kodein Aware activity, has references to data repository and factory instance,
 * which is used by the extended activities
 */
open class BaseActivity : AppCompatActivity(), KodeinAware {
    override val kodein by kodein()
    // data repository instance
    val dataRepository by instance<DataRepository>()
    // application view model factory instance
    val factory by instance<ApplicationViewModelFactory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set the orientation to Portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}