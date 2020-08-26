package com.app.albumsearch.repository.network

import com.app.albumsearch.repository.network.model.AlbumSearchResponse
import retrofit2.http.*


/**
 * Retrofit service interface, which provides the interface for various web service api endpoints
 */
interface RetrofitAPIService {
    @GET("search")
    suspend fun searchAlbums(
        @Query("term") term : String
      ): AlbumSearchResponse

/* @GET("search")
    suspend fun searchAlbums(
        @Query("term") term : String,
        artistName: String,
        trackName: String,
        collectioName: String,
        collectionPrice: String,
        releasedate: String): String*/
}