package com.example.progettowebemobile.principale.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.R

class RestaurantAdapter(
    var restaurants: List<Restaurant>
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>(){

    inner class  RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item, parent, false)
        return RestaurantViewHolder(view)

    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {

        holder.itemView.apply {

        }

    }

    override fun getItemCount(): Int {
        return restaurants.size



    }
}