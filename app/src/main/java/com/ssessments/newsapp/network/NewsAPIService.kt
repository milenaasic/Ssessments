package com.ssessments.newsapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL ="https://dev-ssapi.upconfig.com/"

private val moshi= Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

private val retrofit=Retrofit.Builder()
                        .addConverterFactory(MoshiConverterFactory.create(moshi))
                        .addCallAdapterFactory(CoroutineCallAdapterFactory())
                        .baseUrl(BASE_URL)
                        .build()

interface NewsAPIService {



    @POST("api/v1/posts/get-filtered-list")
    fun postFilteredNewsList(@Body filter:NetworkNewsFilterObject): Deferred<List<NetworkNewsItem>>


    @POST("api/v1/posts/get-by-id")
    fun postSingleNews(@Body news_request:NetworkSingleNewsRequest):Deferred<NetworkSingleNewsItem>


    @POST(" api/v1/posts/get-custom-list")
    fun postCustomSearchNewsList(@Body customSearch:NetworkCustomSearchFilterObject):Deferred<List<NetworkNewsItem>>



    //User LogIn
    @POST("api/v1/users/login")
    fun postUserLogIn(@Body userData: NetworkUserData):Deferred<NetworkUserDataResponse>

    @POST("api/v1/users/forgot-password")
    fun postForgotPassword(@Body user_email:NetworkForgotPasswordRequest):Deferred<NetworkForgotPasswordResponse>

    @POST("api/v1/users/register")
    fun postUserRegistration(@Body userRegistrationData: NetworkUserRegistrationData):Deferred<NetworkUserRegistrationResponse>

    //send notification preferences to server
    @POST("url6")
    fun sendNotificationPreferencesToServer(@Body notifications:NetworkPreferencesObject):Deferred<String>

    @POST("url7")
    fun getNotificationPreferencesFromServer(@Body token:NetworkRequestGetNotifPref):Deferred<List<NetworkSinglePreference>>


}


object NewsApi {
    val retrofitService : NewsAPIService by lazy {
        retrofit.create(NewsAPIService::class.java)
    }
}