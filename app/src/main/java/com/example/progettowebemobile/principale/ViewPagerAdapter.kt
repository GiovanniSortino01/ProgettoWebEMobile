package com.example.progettowebemobile.principale

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.R

class ViewPagerAdapter(private  var images: List<Int>) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val itemImage: ImageView = itemView.findViewById(R.id.viewPager_imageView)

        init {

            itemImage.setOnClickListener { v: View ->
            val position = adapterPosition
            Toast.makeText(itemView.context,"You clicked on item #${position +1}",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.Pager2ViewHolder {

        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager,parent,false))

    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {

        holder.itemImage.setImageResource(images[position])

    }

    override fun getItemCount(): Int {
    return images.size
    }
}