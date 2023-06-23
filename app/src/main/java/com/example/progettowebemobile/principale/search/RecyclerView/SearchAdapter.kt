package com.example.progettowebemobile.principale.search.RecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.SearchItemBinding
import com.example.progettowebemobile.principale.SearchFragment



class SearchAdapter(private val mList: List<ItemsViewModelSearch>,private val fragmentManager: FragmentManager) : RecyclerView.Adapter<SearchAdapter.ViewHolder>(){
    private var itemClickListener: ((Object) -> Unit)? = null
    private var onClickListener: OnClickListener?= null


    class ViewHolder(binding: SearchItemBinding):RecyclerView.ViewHolder(binding.root){
        val imageView = binding.searchImageView
        val name = binding.searchNameTextView
        val luogo = binding.searchLocationTextView
        val recenzioni = binding.searchRatingBar
        val cv_search = binding.cvSearch
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
        holder.recenzioni.rating=ItemsViewModelSearch.recenzione
        holder.itemView.setOnClickListener{
            onClickListener?.onClick(position,ItemsViewModelSearch)
        }
        holder.cv_search.setOnClickListener{

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
    fun setOnItemClickListener(listener: (Object) -> Unit) {
        itemClickListener = listener
    }

}