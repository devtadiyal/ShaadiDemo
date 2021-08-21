package com.example.medcords.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.medcords.model.Result
import com.example.medcords.paging.PhotoDataSource
import com.example.medcords.paging.PhotoDataSourceFactory

class PhotoViewModel : ViewModel() {

    var itemDataSourceFactory :PhotoDataSourceFactory
    var userPagedList: LiveData<PagedList<Result>>
    private var liveDataSource: LiveData<PhotoDataSource>
    init {
        itemDataSourceFactory = PhotoDataSourceFactory("london","297osa_zHv-Zeo5GlmDeHgW2j_f8jpDk1tHcrxaaMa4")
        liveDataSource = itemDataSourceFactory.userLiveDataSource
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(PhotoDataSource.PAGE_SIZE)
            .build()
        userPagedList = LivePagedListBuilder(itemDataSourceFactory, config)
            .build()
    }

    fun refresh() {
        itemDataSourceFactory.userLiveDataSource.getValue()?.invalidate()
    }
}