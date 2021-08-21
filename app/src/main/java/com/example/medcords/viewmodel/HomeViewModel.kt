package com.example.medcords.viewmodel

import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medcords.model.RandomPhotoResponse
import com.example.medcords.network.Resource
import com.example.medcords.repository.Repository
import kotlinx.coroutines.launch


class HomeViewModel(private var repository: Repository) : ViewModel() {
    val loading = MutableLiveData<Boolean>()
    private val randomPhoto: MutableLiveData<Resource<RandomPhotoResponse>> = MutableLiveData()
    val getRandomPhoto: LiveData<Resource<RandomPhotoResponse>>
        get() = randomPhoto

    fun getRandomPhoto() = viewModelScope.launch {
        loading.value = true
        randomPhoto.value = repository.getRandomPhoto()
        loading.value = false
    }

}
