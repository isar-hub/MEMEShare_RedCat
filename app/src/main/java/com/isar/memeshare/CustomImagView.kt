package com.isar.memeshare

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView

class CustomImagView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyle) {

    init {
        // Initialize any custom attributes if needed
    }

    override fun performClick(): Boolean {
        super.performClick()
        // Handle the click action if needed
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // Handle touch events here
        return super.onTouchEvent(event)
    }
}