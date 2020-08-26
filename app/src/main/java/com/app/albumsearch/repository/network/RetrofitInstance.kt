package com.app.albumsearch.repository.network

import androidx.databinding.library.BuildConfig
import com.app.albumsearch.utilis.Constants
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit Singleton Instance
 */
object RetrofitInstance {

    private const val TIMEOUT = 30L // in seconds

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor())
        // http logging interceptor is added to print the logs,
        // this prints log only in debug mode
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                /*HttpLoggingInterceptor.Level.BODY*/ HttpLoggingInterceptor.Level.NONE
            } else HttpLoggingInterceptor.Level.NONE
        })
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS) //Backend is really slow
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .build()


    val retrofitAPIService: RetrofitAPIService by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(RetrofitAPIService::class.java)
    }
}