package com.isar.memeshare

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FavouritesAdapter(private val context: Context,private val items: List<String>
) : RecyclerView.Adapter<FavouritesAdapter.FavViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_spot, parent, false)
        return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {

        val url : String = items[position]
        holder.bind(url,context)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class FavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView : ImageView = itemView.findViewById(R.id.item_image)

        fun bind(url: String,context: Context) {
           Glide.with(context).load(url).placeholder(R.drawable.placeholder).into(imageView)
        }
    }
}