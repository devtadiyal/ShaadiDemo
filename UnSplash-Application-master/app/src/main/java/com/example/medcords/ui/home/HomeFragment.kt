package com.example.medcords.ui.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.medcords.R
import com.example.medcords.adapter.PhotoAdapter
import com.example.medcords.model.Result
import com.example.medcords.network.Resource
import com.example.medcords.viewmodel.AuthViewModelFactory
import com.example.medcords.viewmodel.HomeViewModel
import com.example.medcords.viewmodel.PhotoViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import retrofit2.HttpException


class HomeFragment : Fragment(), KodeinAware {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var photoViewModel: PhotoViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var photoDetails: PagedList<Result>

    //Kodein DI injecting factory class instance
    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var v: View = inflater.inflate(R.layout.home_fragment, container, false)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        photoViewModel = ViewModelProvider(this).get(PhotoViewModel::class.java)
        return v
    }

    //method to get random photo response from viewmodel and load image url in glide
    private fun getRandomPhoto() {
        //saving data into jetpack datasource getting data in mainactivity class
        //  lifecycleScope.launch { preferences.saveData("Data is saved in Jetpack Data Source") }
        //hit api
        homeViewModel.getRandomPhoto()
        //getting response from api
        homeViewModel.getRandomPhoto.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    //Glide handle image caching and image resizing by default
                    Glide.with(requireContext()) //7
                        .load(it.value.urls.regular)
                        .listener(object : RequestListener<Drawable> { //9
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: com.bumptech.glide.request.target.Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                return false
                            }
                        })
                        .into(randomImage)
                }
                is Resource.Failure -> {
                    when (it) {
                        is HttpException -> {
                            Resource.Failure(false, it.code(), it.response()?.errorBody())
                        }
                        else -> {
                            Resource.Failure(true, null, null)
                        }
                    }
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //call random photo api method
        getRandomPhoto()

        //shimmer for photos list
        shimmer_pics_list.visibility = View.VISIBLE
        shimmer_pics_list.startShimmerAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            shimmer_pics_list.stopShimmerAnimation()
            shimmer_pics_list.visibility = View.GONE
            //get photos list
            loadPhotosList()
        },5000)

        //refresh data on pull list
        swipeRefresh.setOnRefreshListener {
            //shimmer for photos list
            shimmer_pics_list.visibility = View.VISIBLE
            shimmer_pics_list.startShimmerAnimation()
            // The method calls setRefreshing(false) when it's finished.
            photoViewModel.refresh()
            swipeRefresh.isRefreshing = false
            Handler(Looper.getMainLooper()).postDelayed({
                shimmer_pics_list.stopShimmerAnimation()
                shimmer_pics_list.visibility = View.GONE

            },3500)

        }

        recycler_view_photos.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                //open details fragment
                var direction =
                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(photoDetails[position] as Result)
                Navigation.findNavController(view).navigate(direction)
            }
        })
    }


    fun loadPhotosList() {
        //adapter of list
        val userAdapter = PhotoAdapter()
        linearLayoutManager = LinearLayoutManager(context)
        recycler_view_photos.layoutManager = linearLayoutManager

        photoViewModel.userPagedList.observe(requireActivity(), Observer {
            if (it != null) {
                photoDetails = it
                userAdapter.submitList(it)
            }
        })
        recycler_view_photos.adapter = userAdapter
    }

    //recyclerview item listener
    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener({
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                })
            }
        })
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }
}