package com.ssessments.newsapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL ="http://192.168.1.6:3000/"

private val moshi= Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

private val retrofit=Retrofit.Builder()
                        .addConverterFactory(MoshiConverterFactory.create(moshi))
                        .addCallAdapterFactory(CoroutineCallAdapterFactory())
                        .baseUrl(BASE_URL)
                        .build()

interface NewsAPIService {
    //
    @GET("rows")
    fun getTestValuesFromLocalServer():Deferred<Response<List<NetworkNewsItem>>>


    //VESTI- ucitaj listu vesti prema datom filteru, podrazumeva i kompletnu listu vesti
    @POST("api/v1/posts/get-filtered-list")
    fun postFilteredNewsList(@Body filter:NetworkNewsFilterObject): Deferred<Response<NetworkNewsListResponseWrapper>>

    //VESTI - ucitaj pojedinacnu vest
    @POST("api/v1/posts/get-by-id")
    fun postSingleNews(@Body news_request:NetworkSingleNewsRequest):Deferred<Response<NetworkSingleNewsItem>>

    //Vesti - ucitaj listu prema custom search-u
    @POST(" api/v1/posts/get-custom-list")
    fun postCustomSearchNewsList(@Body customSearch:NetworkCustomSearchFilterObject):Deferred<Response<NetworkNewsListResponseWrapper>>

    //FILTERI-ucitaj sve predefinisane filtere, za sve korisnike je isto, token ostaje za kasnije
    //@GET("url4")
    //fun getPredefinedFilters(@Body user_token:String):Deferred <List<NetworkNewsFilterObject>>

    //User LogIn
    @POST("api/v1/users/login")
    fun postUserLogIn(@Body userData: NetworkUserData):Deferred<Response<NetworkUserDataResponse>>

    @POST("api/v1/user/forgot-password")
    fun postForgotPassword(@Body user_email:NetworkForgotPasswordRequest):Deferred<Response<NetworkForgotPasswordResponse>>

    @POST(" api/v1/users/register")
    fun postUserRegistration(@Body userRegistrationData: NetworkUserRegistrationData):Deferred<Response<NetworkUserRegistrationResponse>>

    //send notification preferences to server
    @POST("url6")
    fun sendNotificationPreferencesToServer(@Body notifications:NetworkNotificatiosObject):Deferred<String>


}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object NewsApi {
    val retrofitService : NewsAPIService by lazy {
        retrofit.create(NewsAPIService::class.java)
    }
}