package com.example.progettowebemobile.principale.search.RecyclerView.Place

import android.graphics.Bitmap
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.R
class ViewPagerAdapter(private val images: List<Bitmap>) :
    RecyclerView.Adapter<ViewPagerAdapter.PagerViewHolder>() {

    inner class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.viewPager_imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_viewpager, parent, false)
        return PagerViewHolder(view)
    }


    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val image = images[position]
        holder.imageView.setImageBitmap(image)
    }

    override fun getItemCount(): Int {
        return images.size
    }
}
