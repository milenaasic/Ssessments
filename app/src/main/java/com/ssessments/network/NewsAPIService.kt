package com.ssessments.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ssessments.database.UserData
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL ="https://mars.udacity.com/"

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

     */

    //VESTI - ucitaj kompletnu listu vesti
    @GET("url1")
    fun getNewsList( ):Deferred <List<NetworkNewsItem>>

    //VESTI- ucitaj listu vesti prema datom filteru
    @FormUrlEncoded
    @POST("url2")
    fun postFilteredNewsList(@Field("token") usertoken:String,
                             @Body filter:NetworkNewsFilterObject): Deferred<List<NetworkNewsItem>>

    //VESTI - ucitaj pojedinacnu vest, salje se id kao path i token u body
    @POST("url3/{singleNewsID}")
    fun postSingleNews(@Path("singleNewsID") newsID:Int,
                       @Body user_token:String):Deferred<NetworkSingleNewsItem>

    //FILTERI-ucitaj sve filtere zapamcene na serveru, token je u body
    @POST("url4")
    fun postPredefinedFilters(@Body user_token:String):Deferred <List<NetworkNewsFilterObject>>

    //User LogIn
    //sta se vraca sa servera u slucaju uspeha? Token
    @POST("ur4")
    fun postUserLogIn(@Body userData: NetworkUserData):Deferred<String>

    @POST("url6")
    fun postUserRegistration(@Body userRegistrationData: NetworkUserRegistrationData):Deferred<NetworkUserData>

}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object NewsApi {
    val retrofitService : NewsAPIService by lazy {
        retrofit.create(NewsAPIService::class.java)
    }
}