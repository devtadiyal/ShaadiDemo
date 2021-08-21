package com.example.medcords.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medcords.repository.Repository

//I created this ViewModelFactory because i want to send repository instance to ViewModel
//I can't send instance of viewmodel from loginActivity where we getting instance of viewmodel using ViewModelProvider
class AuthViewModelFactory(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}