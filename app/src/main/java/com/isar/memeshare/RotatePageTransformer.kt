package com.isar.memeshare

import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

class RotatePageTransformer : ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        // Only animate the page that is visible

        if (position < -1 || position > 1) {
            view.alpha = 0f
            view.rotationY = -180f
            view.rotation
        } else {
            // Apply the animations based on the position of the page
            view.alpha = 1 - abs(position)
            view.rotationY = position * -180f

            // You may need to set the pivot to avoid rotation issues
            view.pivotX = view.width / 2f
            view.pivotY = view.height / 2f
        }
    }
}