package com.example.progettowebemobile.principale.search.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.SearchItemBinding

class SearchAdapter(private val mList: List<ItemsViewModelSearch>) : RecyclerView.Adapter<SearchAdapter.ViewHolder>(){

    private var onClickListener: OnClickListener?= null

    class ViewHolder(binding: SearchItemBinding):RecyclerView.ViewHolder(binding.root){
        val imageView = binding.imageView
        val name = binding.nameTextView
        val luogo = binding.locationTextView
        val recenzioni = binding.reviewsTextView
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val view = SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val ItemsViewModelSearch = mList[position]
        holder.imageView.setImageBitmap(ItemsViewModelSearch.image)
        holder.name.text=ItemsViewModelSearch.name
        holder.luogo.text=ItemsViewModelSearch.luogo
        holder.itemView.setOnClickListener{
            onClickListener?.onClick(position,ItemsViewModelSearch)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnClickListener{
        fun onClick(position:Int, model:ItemsViewModelSearch)
    }

    fun setOnClickListener(onClickListener:OnClickListener){
        this.onClickListener =  onClickListener
    }

}