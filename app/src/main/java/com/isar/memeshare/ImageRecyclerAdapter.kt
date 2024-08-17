package com.isar.memeshare

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageRecyclerAdapter(private val items: List<MainActivity.Category>, private val context: Context) :
    RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val imageView: ImageView = view.findViewById(R.id.categoryImage)
        val textView: TextView = view.findViewById(R.id.categoryText)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_categories_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.name
        Glide.with(context).load(item.url).placeholder(R.drawable.placeholder).into(holder.imageView)
    }

    override fun getItemCount(): Int = items.size






}

