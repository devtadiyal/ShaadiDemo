package com.example.medcords.network

import com.example.medcords.model.RandomPhotoResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    //send request for random image
    @GET("?results=10")
    suspend fun getRandomPhoto(): RandomPhotoResponse

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
                .baseUrl("https://randomuser.me/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }

}