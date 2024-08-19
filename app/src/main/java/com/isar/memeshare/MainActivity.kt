package com.isar.memeshare


import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.isar.memeshare.databinding.ActivityMainBinding
import com.isar.memeshare.retrofit.Repository
import com.isar.memeshare.viewmodel.MainViewModel
import com.mig35.carousellayoutmanager.CarouselLayoutManager
import com.mig35.carousellayoutmanager.CarouselZoomPostLayoutListener
import com.mig35.carousellayoutmanager.CenterScrollListener


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private val repository = Repository()
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(repository) }
    private lateinit var categoryAdapter: ImageRecyclerAdapter
    private val list = mutableListOf<String>()
    private var doubleClickLastTime = 0L

    var currUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
        viewModel.loadMeme()

        binding.navigationButton.setOnClickListener {
            showDialog()
        }

//        binding.main.setOnClickListener {
//            if(System.currentTimeMillis() - doubleClickLastTime < 300){
//                doubleClickLastTime = 0
//                viewModel.onDoubleTapLike(currUrl)
//                binding.heartIcon.visibility = VISIBLE
//                animateHeartIcon()
//                Toast.makeText(this@MainActivity,"Liked",Toast.LENGTH_SHORT).show()
//            }else{
//                doubleClickLastTime = System.currentTimeMillis()
//            }
//        }


        binding.memeImage.setOnTouchListener(object : OnTouchListener {
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
                            } else {
                                onSwipeLeft()
                            }
                        }
                        return true
                    }
                }
                return false
            }


        });

        binding.heartIcon.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                viewModel.onDoubleTapLike(currUrl)
                binding.heartIcon.visibility = VISIBLE
                animateHeartIcon()
            }
        })
        binding.likeText.setOnClickListener(object : DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                viewModel.onDoubleTapLike(currUrl)
                binding.heartIcon.visibility = VISIBLE
                animateHeartIcon()
            }
        })


    }

    private fun animateHeartIcon() {
        val scaleAnimation = ScaleAnimation(
            0.5f, 1.5f,
            0.5f, 1.5f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.duration = 300
        scaleAnimation.repeatCount = 1
        scaleAnimation.repeatMode = Animation.REVERSE

        binding.heartIcon.startAnimation(scaleAnimation)

    }

    private fun setupObservers() {
        viewModel.memeUrl.observe(this) { url ->
            url?.let {
                currUrl = it
                loadImage(it)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    private fun showLoading() {
        val rotate = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 1000
            repeatMode = Animation.RESTART
            repeatCount = Animation.INFINITE
        }
        binding.loader.startAnimation(rotate)
        binding.loader.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loader.clearAnimation()
    }

    private fun loadImage(url: String) {
        val fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 500
        }
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                Glide.with(this@MainActivity)
                    .load(url)
                    .thumbnail(0.1f)
                    .into(binding.memeImage)
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        binding.memeImage.startAnimation(fadeOut)
    }

    private fun setupRecyclerView() {
        val categoryLayoutManager = CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL, true)
        categoryLayoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        categoryAdapter = ImageRecyclerAdapter(getCategoryData(), this)
        binding.categories.apply {
            layoutManager = categoryLayoutManager
            adapter = categoryAdapter
            addOnScrollListener(CenterScrollListener())
        }
        categoryAdapter.onItemClick = { items ->

            Toast.makeText(this@MainActivity, "Clicked ON ${items.name}",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog() {
        val dialog = BottomSheetDialog(this, R.style.TransparentBottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.nav_header, null)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler)

        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager

        // Observe list and update adapter
        viewModel.list.observe(this, Observer { items ->
            val navAdapter = FavouritesAdapter(applicationContext, items)
            recyclerView.adapter = navAdapter
        })

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(view)
        dialog.show()
        Toast.makeText(this@MainActivity, "Dialog displayed", Toast.LENGTH_SHORT).show()
    }


    private fun onSwipeLeft() {
        viewModel.loadMeme()

    }

    private fun onSwipeRight() {
        viewModel.loadMeme()
    }

    private fun getCategoryData(): List<Category> {
        // Replace with your method to fetch or generate data for the adapter
        return listOf(
            Category("Memes", R.drawable.memes),
            Category("Inspirational Quotes", R.drawable.motivational_quotes),
            Category("Quotes", R.drawable.quotes),
            Category("Poetry", R.drawable.poetry),
            Category("Motivations", R.drawable.motivations),
            Category("Facts", R.drawable.facts),
            Category("Career Advice", R.drawable.development),
            Category("Breaking News", R.drawable.breaking_news),
            Category("Art and Design", R.drawable.art),
            Category("Photography", R.drawable.photography),
            Category("Mental Health Awareness", R.drawable.health),
            Category("Meditation and Mindfulness", R.drawable.meditation),
            Category("Financial Tips", R.drawable.financial),
            Category("Home Decor Ideas", R.drawable.home_decor),
            Category("Food Recipes", R.drawable.food_recipie),
            Category("Health and Wellness Tips", R.drawable.health_wellness),
            Category("Travel Diaries", R.drawable.travel),
            Category("Fashion Trends", R.drawable.fashion_trends),
            Category("Science Facts", R.drawable.science),
            Category("Cartoons/Comics", R.drawable.comics),
            Category("Personal Development Tips", R.drawable.personal_dev),
            Category("Fitness Motivation", R.drawable.motivational),

            Category("Music", R.drawable.music),
            Category("GIFs", R.drawable.gifs),
            Category("Movie/TV Show Updates", R.drawable.movie_tv),
            Category("Sports Updates", R.drawable.sports),
            Category("Politics", R.drawable.politics),


            )


    }


    class MainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    data class Memes(val url: String)
    data class Category(val name: String, val url: Int)


    abstract class DoubleClickListener : OnClickListener {
        private var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v)
            }
            lastClickTime = clickTime

        }

        abstract fun onDoubleClick(v: View?)

        companion object {
            private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 //milliseconds
        }
    }

}