package com.isar.memeshare

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.isar.memeshare.retrofit.RetrofitInstance
import com.mig35.carousellayoutmanager.CarouselLayoutManager
import com.mig35.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.mig35.carousellayoutmanager.CenterScrollListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.RequestListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {



    lateinit var categoryLayoutManager: CarouselLayoutManager
    lateinit var categoryAdapter: ImageRecyclerAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var imageView : CustomImagView
    lateinit var progressBar : ProgressBar

    private val imageUrlList = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        progressBar = findViewById<ProgressBar?>(R.id.progressBar).apply {
            visibility = VISIBLE
        }
        imageView = findViewById(R.id.card_stack_view)

        imageView.setOnTouchListener(object : OnTouchListener {
            private var x1: Float = 0f
            private var x2: Float = 0f
            private var y1: Float = 0f
            private var y2: Float = 0f
            private val SWIPE_THRESHOLD = 100

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        x1 = event.x
                        y1 = event.y
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        x2 = event.x
                        y2 = event.y
                        val diffX = x2 - x1
                        val diffY = y2 - y1

                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(diffX) > Math.abs(diffY)) {
                            if (diffX > 0) {
                                onSwipeRight()
                            }
                        }
                        return true
                    }
                }
                return false
            }


        });

            loadNewMeme()


//        if (first) {
//            loadNewMeme()
//            first=false;
//        }
//        manager = CardStackLayoutManager(applicationContext,this)
//        cardAdapter = CardStackAdapter(imageUrlList,this)
//         cardStackView = findViewById<CardStackView?>(R.id.card_stack_view).apply {
//             layoutManager = manager
//         }


        recyclerView = findViewById(R.id.categories)
        categoryLayoutManager =   CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true)
        categoryLayoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        categoryAdapter = ImageRecyclerAdapter(getCategoryData(),this@MainActivity)
        recyclerView .apply {
            layoutManager = categoryLayoutManager
            adapter = categoryAdapter
            addOnScrollListener(CenterScrollListener())
        }

//        setupCardStackView()


    }
    private fun onSwipeLeft() {

    }
    private fun onSwipeRight() {
        loadNewMeme()
    }

    private fun getCategoryData(): List<Category> {
        // Replace with your method to fetch or generate data for the adapter
        return listOf(
            Category("Item 1", "https://api.memegen.link/images/ds/small_file/high_quality.png"),
            Category("Item 2", "https://picsum.photos/seed/picsum/200/300"),
            Category("Item 3",   "https://picsum.photos/200/300?grayscale"),
            Category("Item 4",   "https://picsum.photos/200/300/?blur"),
            Category("Item 5",     "https://picsum.photos/id/870/200/300?grayscale&blur=2"),
            Category("Item 6",    "https://picsum.photos/200/300?grayscale"),
            Category("Item 7",   "https://api.memegen.link/images/ds/small_file/high_quality.png"),
            Category("Item 8",   "https://picsum.photos/200/300/?blur"),
            Category("Item 9", "https://picsum.photos/200/300?grayscale"),
            Category("Item 10",   "https://picsum.photos/200/300/?blur"),
            Category("Item 11",    "https://api.memegen.link/images/ds/small_file/high_quality.png")
        )



    }

//
//    private fun setupCardStackView() {
//        initialize()
//    }

//    private fun paginate() {
//        val old = cardAdapter.getSpots()
//        val new = old.plus(createSpots())
//        val callback = SpotDiffCallback(old, new)
//        val result = DiffUtil.calculateDiff(callback)
//        cardAdapter.setSpots(new)
//        result.dispatchUpdatesTo(cardAdapter)
//    }


//    private fun initialize() {
//        manager.setStackFrom(StackFrom.None)
//        manager.setVisibleCount(3)
//        manager.setTranslationInterval(8.0f)
//        manager.setScaleInterval(0.95f)
//        manager.setSwipeThreshold(0.3f)
//        manager.setMaxDegree(20.0f)
//        manager.setDirections(Direction.HORIZONTAL)
//        manager.setCanScrollHorizontal(true)
//        manager.setCanScrollVertical(true)
//        manager.setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
//        manager.setOverlayInterpolator(LinearInterpolator())
//        cardStackView.layoutManager = manager
//        cardStackView.adapter = cardAdapter
//
//    }

//
//
//    override fun onCardDragging(direction: Direction, ratio: Float) {
//        Log.d("CardStackView", "onCardDragging: d = ${direction.name}, r = $ratio")
//    }
    private fun loadNewMeme() {
        progressBar.visibility = VISIBLE
    // Make the API call
    RetrofitInstance.getApiInterface().getMemes().enqueue(object : retrofit2.Callback<Memes> {
        override fun onResponse(call: Call<Memes>, response: Response<Memes>) {
            if (response.isSuccessful) {
                val memeResponse = response.body()
                memeResponse?.let {
                    val url = it.url
                    Glide.with(this@MainActivity).load(url).listener(object:RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = GONE
                            return false
                        }
                    }).into(imageView)
                }
            } else {
                progressBar.visibility = VISIBLE
                Toast.makeText(this@MainActivity, "Response error", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<Memes>, t: Throwable) {
            Toast.makeText(this@MainActivity, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
    }

//    override fun onCardSwiped(direction: Direction) {
//        loadNewMeme()
//        Log.d("CardStackView", "onCardSwiped: p = ${manager.topPosition}, d = $direction")
//    }
//
//    override fun onCardRewound() {
//        Log.d("CardStackView", "onCardRewound: ${manager.topPosition}")
//    }
//
//    override fun onCardCanceled() {
//        Log.d("CardStackView", "onCardCanceled: ${manager.topPosition}")
//    }
//
//    override fun onCardAppeared(view: View, position: Int) {
//    }
//
//    override fun onCardDisappeared(view: View, position: Int) {
//    }
//
}

data class Memes(val url : String)
data class Category(val name : String, val url : String)
