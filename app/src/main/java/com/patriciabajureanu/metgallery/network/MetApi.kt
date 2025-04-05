package com.patriciabajureanu.metgallery.network

import android.content.Context

import com.patriciabajureanu.metgallery.artworks.api.MetArtwork
import com.patriciabajureanu.metgallery.artworks.api.SearchArtwork
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit
import com.squareup.moshi.Moshi


class AppUser(context: Context) {

    private val okHttpClient = OkHttpClient.Builder()
        .cache(Cache(context.cacheDir, (100 * 1024 * 1024).toLong()))
        .addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor { chain ->
            val cacheControl = CacheControl.Builder()
                .maxAge(30, TimeUnit.MINUTES)
                .maxStale(7, TimeUnit.DAYS)
                .build()
            val request = chain.request().newBuilder().cacheControl(cacheControl).build()
            chain.proceed(request)
        }
        .build()
    val moshi = Moshi.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://collectionapi.metmuseum.org/public/collection/v1/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val metApi = retrofit.create(MetApi::class.java)
}

interface MetApi {
    @GET("objects")
    suspend fun getArtworks(): Response<SearchArtwork>
    @GET("search")
    suspend fun searchArtwork(@Query("q") query: String): Response<SearchArtwork>
    @GET("objects/{id}")
    suspend fun getArtwork(@Path("id") id: Int): Response<MetArtwork>
}