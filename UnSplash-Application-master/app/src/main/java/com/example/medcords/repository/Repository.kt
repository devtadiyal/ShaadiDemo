package com.example.medcords.repository

import com.example.medcords.model.PhotosResponse
import com.example.medcords.model.random.RandomPhotoResponse
import com.example.medcords.network.Api

class Repository(private var api:Api):BaseRepository() {


    suspend fun getPhotos(query: String,clientId: String,page:Int,per_page:Int)
            = safeApiCall { api.getPhotosList(query,clientId,page,per_page) }


 suspend fun getRandomPhoto(clientId: String) = safeApiCall {api.getRandomPhoto(clientId) }

    }