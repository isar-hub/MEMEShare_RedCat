package com.isar.memeshare

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ImageRecyclerAdapter(private val items: List<MainActivity.Category>, private val context: Context) :
    RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder>() {
    var onItemClick: ((MainActivity.Category) -> Unit)? = null


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        val imageView: ImageView = view.findViewById(R.id.categoryImage)
        val textView: TextView = view.findViewById(R.id.categoryText)
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(items[bindingAdapterPosition])
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_categories_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.name
        holder.imageView.setImageResource(item.url)

// Set click listener for the item view
//        holder.itemView.setOnClickListener {
//            onClickListener?.onClick(item)
//        }

    }

    override fun getItemCount(): Int = items.size
//
//    fun setOnClickListener(onClickListener : OnClickListener) {
//        this.onClickListener = onClickListener
//    }
//    interface OnClickListener {
//        fun onClick( items: MainActivity.Category   )
//    }



}

