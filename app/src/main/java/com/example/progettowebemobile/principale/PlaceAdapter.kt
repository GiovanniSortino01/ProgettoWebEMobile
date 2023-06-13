package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.R

class PlaceAdapter(
    var places: List<Place>
) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>(){

    inner class  PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return PlaceViewHolder(view)

    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {

        holder.itemView.apply {

        }

    }

    override fun getItemCount(): Int {
        return places.size



    }
}