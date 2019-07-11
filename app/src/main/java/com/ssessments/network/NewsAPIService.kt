package com.ssessments.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL ="https://mars.udacity.com"

private val moshi= Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

private val retrofit=Retrofit.Builder()
                        .addConverterFactory(MoshiConverterFactory.create(moshi))
                        .addCallAdapterFactory(CoroutineCallAdapterFactory())
                        .baseUrl(BASE_URL)
                        .build()

interface NewsAPIService {
    /**
     * Returns a Coroutine [Deferred] [List] of [MarsProperty] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("realestate")
    fun getProperties( ):Deferred<List<NewsItem>>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object NewsApi {
    val retrofitService : NewsAPIService by lazy { retrofit.create(NewsAPIService::class.java) }
}