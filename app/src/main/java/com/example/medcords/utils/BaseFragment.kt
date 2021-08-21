package com.example.medcords.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//class is abstract because we can't instantiate but only inherit
abstract class BaseFragment<DB : ViewDataBinding, VM : ViewModel> : Fragment() {
    protected lateinit var binding: DB
    protected lateinit var viewmodel: VM


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            getFragmentView(),
            container,
            false
        )
        viewmodel = ViewModelProvider(this).get(getViewModel())
        return binding.root
    }

    abstract fun getViewModel(): Class<VM>
    abstract fun getFragmentView(): Int

    //in fragment do this and implements methods and do all work in onActivityCreated()
    // override fun getViewModel() = QuotesViewModel::class.java
    //    override fun getFragmentView()= R.layout.quote_fragment
}