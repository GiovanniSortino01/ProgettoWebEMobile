package com.example.progettowebemobile.principale.search.RecyclerView.Place

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.databinding.CardRecenzioniBinding
import com.example.progettowebemobile.principale.account.ItemsViewModelPost
import com.example.progettowebemobile.principale.account.PersonalAccountAdapter
import com.example.progettowebemobile.principale.search.RecyclerView.ItemsViewModelSearch

class RecenzioniAdapter (private val mList: ArrayList<ItemsViewModelRecenzioni>) : RecyclerView.Adapter<RecenzioniAdapter.ViewHolder>(){
    private var onClickListener: RecenzioniAdapter.OnClickListener?= null
    class ViewHolder(binding: CardRecenzioniBinding):RecyclerView.ViewHolder(binding.root){
        val nome = binding.textViewAuthorName
        val descrizione = binding.textViewReviewText
        val ratingbar = binding.searchFragmentRatingBar
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecenzioniAdapter.ViewHolder {
        val view = CardRecenzioniBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecenzioniAdapter.ViewHolder, position: Int) {
        val ItemsViewModelRecenzioni = mList[position]
        holder.nome.text = ItemsViewModelRecenzioni.nome
        holder.descrizione.text = ItemsViewModelRecenzioni.descriziona
        holder.ratingbar.rating=ItemsViewModelRecenzioni.recenzioni
        holder.nome.setOnClickListener{
            //spostati nella home dell'utente
        }
    }
    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnClickListener{
        fun onClick(position:Int, model: ItemsViewModelPost)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener =  onClickListener
    }

}
