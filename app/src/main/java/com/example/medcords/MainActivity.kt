package com.example.medcords

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcords.db.AppDatabase
import com.example.medcords.internal.*
import com.example.medcords.model.Result
import com.example.medcords.network.Resource
import com.example.medcords.utils.toast
import com.example.medcords.viewmodel.AuthViewModelFactory
import com.example.medcords.viewmodel.HomeViewModel
import com.example.medcords.viewmodel.UserModelFactory
import com.example.medcords.viewmodel.UserViewModel
import com.yuyakaido.android.cardstackview.sample.CardStackAdapter
import com.yuyakaido.android.cardstackview.sample.Spot
import com.yuyakaido.android.cardstackview.sample.SpotDiffCallback
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import retrofit2.HttpException
import java.util.*


class MainActivity : AppCompatActivity(), CardStackListener, KodeinAware {

    private val drawerLayout by lazy { findViewById<DrawerLayout>(R.id.drawer_layout) }
    private val cardStackView by lazy { findViewById<CardStackView>(R.id.card_stack_view) }
    private val manager by lazy { CardStackLayoutManager(this, this) }
    private val adapter by lazy { CardStackAdapter(createSpots()) }
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var viewModel: UserViewModel
    var resultList = listOf<Spot>()
    var pos = 0

    //private var resultList = listOf<Spot>()
    //Kodein DI injecting factory class instance
    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private val factory2: UserModelFactory by instance()
    private val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        viewModel = ViewModelProvider(this, factory2).get(UserViewModel::class.java)

        //call random photo api method
        getRandomPhoto()
        setupButton()
    }


    //method to get random photo response from viewmodel and load image url in glide
    private fun getRandomPhoto() {
        //saving data into jetpack datasource getting data in mainactivity class
        //  lifecycleScope.launch { preferences.saveData("Data is saved in Jetpack Data Source") }
        //hit api
        homeViewModel.getRandomPhoto()
        //getting response from api
        homeViewModel.getRandomPhoto.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    val spots = ArrayList<Spot>()
                    for (i in 0 until it.value.results!!.size) {
                        spots.add(
                            Spot(
                                name = it.value.results[i].name.first + " " + it.value.results[i].name.last,
                                city = it.value.results[i].location.city + " " + it.value.results[i].location.state,
                                url = it.value.results[i].picture.large,
                                accept = "null",
                                reject = "null"
                            )
                        )

                    }
                    lifecycleScope.launch {
                        AppDatabase(this@MainActivity).getUserDao().insert(spots)
                    }

                    createSpots()
                    // Toast.makeText(this, "HI DEV "+it.value.results.size.toString(), Toast.LENGTH_SHORT).show()
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


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
    }

    override fun onCardSwiped(direction: Direction) {
        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
        if (manager.topPosition == adapter.itemCount - 5) {
            paginate()
        }
    }

    override fun onCardRewound() {
        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
    }

    override fun onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
    }

    override fun onCardAppeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
            toast(resultList.get(position).id.toString())
        pos = position
        Log.d("CardStackView", "onCardAppeared: ($position) ${textView.text}")
    }

    override fun onCardDisappeared(view: View, position: Int) {
        val textView = view.findViewById<TextView>(R.id.item_name)
        Log.d("CardStackView", "onCardDisappeared: ($position) ${textView.text}")
    }

    private fun setupCardStackView() {
        initialize()
    }

    private fun rejectUserSaveInDB(){
        var spot = Spot(resultList.get(pos).id,
            resultList.get(pos).name,
            resultList.get(pos).city,
            resultList.get(pos).url,
            "null",
            "Reject",
        )
        lifecycleScope.launch {
            spot.no = resultList.get(pos).no
            AppDatabase(this@MainActivity).getUserDao().updateUser(spot)
        }
    }
    private fun setupButton() {
        val skip = findViewById<View>(R.id.skip_button)
        skip.setOnClickListener {
            //reject value update in DB
            rejectUserSaveInDB()

            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }

        val rewind = findViewById<View>(R.id.rewind_button)
        rewind.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            manager.setRewindAnimationSetting(setting)
            cardStackView.rewind()
        }

        val like = findViewById<View>(R.id.like_button)
        like.setOnClickListener {
            //accept value update in DB
            acceptUserSaveInDB()

            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            manager.setSwipeAnimationSetting(setting)
            cardStackView.swipe()
        }
    }

    private fun acceptUserSaveInDB() {
        var spot = Spot(resultList.get(pos).id,
            resultList.get(pos).name,
            resultList.get(pos).city,
            resultList.get(pos).url,
            "Accept",
            "null",
        )
        lifecycleScope.launch {
            spot.no = resultList.get(pos).no
            AppDatabase(this@MainActivity).getUserDao().updateUser(spot)
        }
    }

    private fun initialize() {
        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        manager.setCanScrollHorizontal(true)
        manager.setCanScrollVertical(true)
        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
        manager.setOverlayInterpolator(LinearInterpolator())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun paginate() {
        val old = adapter.getSpots()
        val new = old.plus(createSpots())
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun reload() {
        val old = adapter.getSpots()
        val new = createSpots()
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addFirst(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                add(manager.topPosition, createSpot())
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun addLast(size: Int) {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            addAll(List(size) { createSpot() })
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeFirst(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(manager.topPosition)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun removeLast(size: Int) {
        if (adapter.getSpots().isEmpty()) {
            return
        }

        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            for (i in 0 until size) {
                removeAt(this.size - 1)
            }
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun replace() {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            removeAt(manager.topPosition)
            add(manager.topPosition, createSpot())
        }
        adapter.setSpots(new)
        adapter.notifyItemChanged(manager.topPosition)
    }

    private fun swap() {
        val old = adapter.getSpots()
        val new = mutableListOf<Spot>().apply {
            addAll(old)
            val first = removeAt(manager.topPosition)
            val last = removeAt(this.size - 1)
            add(manager.topPosition, last)
            add(first)
        }
        val callback = SpotDiffCallback(old, new)
        val result = DiffUtil.calculateDiff(callback)
        adapter.setSpots(new)
        result.dispatchUpdatesTo(adapter)
    }

    private fun createSpot(): Spot {
        //Default
        return Spot(
            name = "Yasaka Shrine",
            city = "Kyoto",
            url = "https://source.unsplash.com/Xq1ntWruZQI/600x800",
            accept = "A",
            reject = "R"
        )
    }

    private fun createSpots(): List<Spot> {
        viewModel.getUsers()
        viewModel.users.observe(this, Observer { users ->
            resultList = users
            //System.out.println("SIZE RESUME " + users.size)
            adapter.setSpots(users)
            setupCardStackView()
            adapter.notifyDataSetChanged()
        })
        //System.out.println("SIZE RESUME A " + resultList.size)

        return resultList

    }

}