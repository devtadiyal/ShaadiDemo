package com.example.medcords.paging

import androidx.paging.PageKeyedDataSource
import com.example.medcords.model.PhotosResponse
import com.example.medcords.model.Result
import com.example.medcords.network.Api
import com.example.medcords.network.ApiServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PhotoDataSource(private var city:String, private var client_id:String) : PageKeyedDataSource<Int, Result>() {
  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
    val service = ApiServiceBuilder.buildService(Api::class.java)
    val call = service.getPagingPhotos(city,client_id,params.key,PAGE_SIZE)
    call.enqueue(object : Callback<PhotosResponse> {
      override fun onResponse(call: Call<PhotosResponse>, response: Response<PhotosResponse>) {
        if (response.isSuccessful) {
          val apiResponse = response.body()!!
          val responseItems = apiResponse.results
          val key = if (params.key > 1) params.key - 1 else 0
          responseItems?.let {
            callback.onResult(responseItems, key)
          }
        }
      }
      override fun onFailure(call: Call<PhotosResponse>, t: Throwable) {
      }
    })
  }
  override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Result>) {
    val service = ApiServiceBuilder.buildService(Api::class.java)
    val call = service.getPagingPhotos(city,client_id,
      FIRST_PAGE,PAGE_SIZE)
    call.enqueue(object : Callback<PhotosResponse> {
      override fun onResponse(call: Call<PhotosResponse>, response: Response<PhotosResponse>) {
        if (response.isSuccessful) {
          val apiResponse = response.body()!!
          val responseItems = apiResponse.results
          responseItems?.let {
            callback.onResult(responseItems, null, FIRST_PAGE + 1)
          }
        }
      }
      override fun onFailure(call: Call<PhotosResponse>, t: Throwable) {
      }
    })
  }
  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
    val service = ApiServiceBuilder.buildService(Api::class.java)
    val call = service.getPagingPhotos(city,client_id,params.key,PAGE_SIZE)
    call.enqueue(object : Callback<PhotosResponse> {
      override fun onResponse(call: Call<PhotosResponse>, response: Response<PhotosResponse>) {
        if (response.isSuccessful) {
          val apiResponse = response.body()!!
          val responseItems = apiResponse.results
          val key = params.key + 1
          responseItems?.let {
            callback.onResult(responseItems, key)
          }
        }
      }
      override fun onFailure(call: Call<PhotosResponse>, t: Throwable) {
      }
    })
  }
  companion object {
    const val PAGE_SIZE = 30
    const val FIRST_PAGE = 1
  }
}