package com.example.medcords.repository

import com.example.medcords.network.Api

class Repository(private var api:Api):BaseRepository() {


 suspend fun getRandomPhoto() = safeApiCall {api.getRandomPhoto() }

    }