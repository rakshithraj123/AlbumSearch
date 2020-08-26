package com.app.albumsearch.utilis

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi
import com.app.albumsearch.repository.network.model.Album
import com.app.albumsearch.repository.network.model.AlbumSearchResponse
import java.text.SimpleDateFormat

object CommonUtilis {
    /**
     * remove duplicate by trackName
     */
    fun removeDuplicateAlbum(response: AlbumSearchResponse) : List<Album>? {
        return response.results?.distinctBy { it.trackName }
    }

    /**
     * sort By ReleaseDate
     */
    fun sortByReleaseDate(response: AlbumSearchResponse) : List<Album>? {
        return response.results?.sortedWith(compareByDescending { getTimeStamp(it.releaseDate) })
    }


    /**
     * sort By CollectionName
     */
    fun sortByCollectionName(response: AlbumSearchResponse) : List<Album>? {
        return response.results?.sortedWith(compareByDescending { it.collectionName })
    }

    /**
     * sort By TrackName
     */
    fun sortByTrackName(response: AlbumSearchResponse) : List<Album>? {
        return response.results?.sortedWith(compareByDescending { it.trackName})
    }

    /**
     * sort By ArtistName
     */
    fun sortByArtistName(response: AlbumSearchResponse) : List<Album>? {
        return response.results?.sortedWith(compareByDescending {it.artistName })
    }

    /**
     * sort By Price
     */
    fun sortByCollectionPrice(response: AlbumSearchResponse) : List<Album>? {
        return response.results?.sortedWith(compareByDescending { it.collectionPrice })
    }

    /**
     * search by trackName,collectionName,collectionPrice,releasedate
     */
    fun search(response: AlbumSearchResponse, artistName: String?,
               trackName: String?,
               collectionName: String?,
               collectionPrice: String?,
               releasedate: String?) : List<Album>? {

        var searchResult :List<Album>? = response.results
        artistName?.let {
            searchResult = searchResult?.filter {
                it.artistName.toLowerCase().contains(artistName.toLowerCase())
            }
        }

        trackName?.let {
            searchResult = searchResult?.filter {
                it.trackName.toLowerCase().contains(trackName.toLowerCase())
            }
        }
        collectionName?.let {
            searchResult = searchResult?.filter {
                it.collectionName.toLowerCase().contains(collectionName.toLowerCase())
            }
        }
        collectionPrice?.let {
            searchResult = searchResult?.filter {
                it.collectionPrice.toString().contains(collectionPrice.toString())
            }
        }
        releasedate?.let {
            searchResult = searchResult?.filter {
                it.releaseDate.toLowerCase().contains(releasedate.toLowerCase())
            }
        }

        return searchResult
    }

    fun getTimeStamp(dateTime: String) : Long {
        return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateTime).time
    }

    /** Checks if the device is connected to internet */
    fun isConnectedToInternet(context: Context): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isOnline(context)
        } else {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetworkInfo: NetworkInfo? = null
            activeNetworkInfo = cm.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        }
        return false
    }
}