package com.example.medcords.network

import com.example.medcords.model.PhotosResponse
import com.example.medcords.model.random.RandomPhotoResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    // google paging library added to handle
    @GET("search/photos")
    fun getPagingPhotos( @Query("query") query: String,
                  @Query("client_id") client: String,
                  @Query("page") page: Int,
                  @Query("per_page") per_page: Int): Call<PhotosResponse>

    //send request for random image
    @GET("photos/random")
    suspend fun getRandomPhoto(
        @Query("client_id") client: String
    ): RandomPhotoResponse

    companion object {
        operator fun invoke(): Api {
            // Interceptor to Log the Request
            val interceptor = HttpLoggingInterceptor()
            // Level.BODY prints Urls, Params and Response
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient =
                OkHttpClient.Builder().addInterceptor(interceptor).build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }

    //without paging handling list
    @GET("search/photos")
    suspend fun getPhotosList(
        @Query("query") query: String,
        @Query("client_id") client: String,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int
    ): PhotosResponse
}