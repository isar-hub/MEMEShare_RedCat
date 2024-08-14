package com.isar.memeshare

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CardStackAdapter(
    private var spots: List<String> = emptyList(),
    private var context : Context
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_spot, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val spot = spots[position]
        Log.e("TEst","List $spots pos : $spot")


        Glide.with(context)
            .load(spot).placeholder(R.drawable.placeholder)
            .into(holder.image)
        holder.itemView.setOnClickListener { v ->
            Toast.makeText(v.context, spot, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return spots.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var image: ImageView = view.findViewById(R.id.item_image)
    }

}
