package com.example.medcords.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.medcords.model.Result
import com.example.medcords.repository.Repository

class DetailsViewModel(private var repository: Repository) : ViewModel(){

    fun closeView(view:View){
        Navigation.findNavController(view).navigateUp()
    }

}