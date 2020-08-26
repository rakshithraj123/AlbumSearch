package com.app.albumsearch.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.albumsearch.utilis.CommonUtilis
import com.app.albumsearch.utilis.Constants
import com.app.albumsearch.R
import com.app.albumsearch.databinding.ActivityMainBinding
import com.app.albumsearch.repository.network.model.AlbumSearchResponse
import com.app.albumsearch.repository.storage.StorageUtils
import com.app.albumsearch.viewmodel.MainViewModel

/**
 * serach album
 */
class MainActivity : BaseActivity(), MainClickListener {
    private var mainBinding: ActivityMainBinding? = null
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        init()

    }

    private fun init() {
        mainViewModel =
            ViewModelProvider(this, factory).get(MainViewModel::class.java)
        mainBinding?.mainClickListener = this
    }


    override fun onSearch(
        artistName: String?,
        trackName: String?,
        collectioName: String?,
        collectionPrice: String?,
        releasedate: String?
    ) {
        when {
            TextUtils.isEmpty(artistName) -> Toast.makeText(
                this,
                getString(R.string.enter_artistName),
                Toast.LENGTH_SHORT
            ).show()
            TextUtils.isEmpty(trackName) -> Toast.makeText(
                this,
                getString(R.string.enter_trackName),
                Toast.LENGTH_SHORT
            ).show()
            else -> {
                if (!CommonUtilis.isConnectedToInternet(this)) {
                    handleOfflineState(
                        artistName,
                        trackName,
                        collectioName,
                        collectionPrice,
                        releasedate
                    )
                } else {
                    mainBinding?.progressBar?.visibility = View.VISIBLE
                    mainViewModel.fetchAlbums().observe(this, Observer {
                        mainBinding?.progressBar?.visibility = View.INVISIBLE
                        handleResponse(
                            it, artistName,
                            trackName,
                            collectioName,
                            collectionPrice,
                            releasedate
                        )
                    })
                }
            }
        }
    }

    override fun onSearchAll() {
        if (!CommonUtilis.isConnectedToInternet(this)) {
            mainViewModel.getAlbum().let {
                CommonUtilis.sortByReleaseDate(it)?.let { sortedResponse ->
                    it.results = sortedResponse
                }
                val intent = Intent(this, SearchResultActivity::class.java)
                intent.putExtra(Constants.KEY_SERACH_RESULT, it)
                startActivity(intent)
            }
        } else {
            mainBinding?.progressBar?.visibility = View.VISIBLE
            mainViewModel.fetchAlbums().observe(this, Observer {
                mainBinding?.progressBar?.visibility = View.INVISIBLE
                StorageUtils.storeAblum(this, it);
                CommonUtilis.sortByReleaseDate(it)?.let { sortedResponse ->
                    it.results = sortedResponse
                }

                val intent = Intent(this, SearchResultActivity::class.java)
                intent.putExtra(Constants.KEY_SERACH_RESULT, it)
                startActivity(intent)
            })
        }
    }

    /**
     * handle reponse received from server
     */
    private fun handleResponse(
        response: AlbumSearchResponse, artistName: String?,
        trackName: String?,
        collectionName: String?,
        collectionPrice: String?,
        releasedate: String?
    ) {
        if (response.resultCount == 0) {
            handleOfflineState(
                artistName,
                trackName,
                collectionName,
                collectionPrice,
                releasedate
            )
        } else {
            //store result for offline usage
            StorageUtils.storeAblum(this, response);
            processResponse(
                response, artistName,
                trackName,
                collectionName,
                collectionPrice,
                releasedate
            ).let { processedResult ->
                val intent = Intent(this, SearchResultActivity::class.java)
                intent.putExtra(Constants.KEY_SERACH_RESULT, processedResult)
                startActivity(intent)
            }
        }
    }

    /**
     * handle display result in offile state
     */
    private fun handleOfflineState(
        artistName: String?,
        trackName: String?,
        collectionName: String?,
        collectionPrice: String?,
        releasedate: String?
    ) {
        val intent = Intent(this, SearchResultActivity::class.java)
        mainViewModel.getAlbum().let { it ->
            processResponse(
                it, artistName,
                trackName,
                collectionName,
                collectionPrice,
                releasedate
            ).let { processedResult ->
                intent.putExtra(Constants.KEY_SERACH_RESULT, processedResult)
            }
        }
        startActivity(intent)
    }

    /**
     * process the response search,sort
     */
    private fun processResponse(
        response: AlbumSearchResponse, artistName: String?,
        trackName: String?,
        collectionName: String?,
        collectionPrice: String?,
        releasedate: String?
    ): AlbumSearchResponse {
        CommonUtilis.search(
            response, artistName,
            trackName,
            collectionName,
            collectionPrice,
            releasedate
        )?.let {
            response.results = it
        }

        CommonUtilis.removeDuplicateAlbum(response)?.let {
            response.results = it
        }
        CommonUtilis.sortByReleaseDate(response)?.let {
            response.results = it
        }

        return response
    }
}


interface MainClickListener {
    /**
     * start search
     */
    fun onSearch(
        artistName: String?,
        trackName: String?,
        collectioName: String?,
        collectionPrice: String?,
        releasedate: String?
    )

    fun onSearchAll()
}