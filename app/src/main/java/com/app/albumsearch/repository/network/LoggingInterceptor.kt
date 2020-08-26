package com.app.albumsearch.repository.network

import android.annotation.SuppressLint
import androidx.databinding.library.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http2.Http2Reader.Companion.logger
import java.lang.String
/**
 * A Log Interceptor which is used to intercept the https request and response objects
 */
internal class LoggingInterceptor : Interceptor {
    @SuppressLint("DefaultLocale")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val t1 = System.nanoTime()

        if (BuildConfig.DEBUG) {
            logger.info(
                String.format(
                    "*** Sending request %s on %s%n%s",
                    request.url, chain.connection(), request.headers
                )
            )
        }

        val response = chain.proceed(request)
        val t2 = System.nanoTime()

        if (BuildConfig.DEBUG) {
            logger.info(
                String.format(
                    "*** Received response for %s in %.1fms%n%s",
                    response.request.url, (t2 - t1) / 1e6, response.headers
                )
            )
        }
        return response
    }
}