package com.example.progettowebemobile.principale.search.RecyclerView.Place

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.databinding.CardRecenzioniBinding
import com.example.progettowebemobile.principale.account.ItemsViewModelPost
import com.example.progettowebemobile.principale.account.PersonalAccountAdapter
import com.example.progettowebemobile.principale.search.RecyclerView.ItemsViewModelSearch

class RecenzioniAdapter (private val mList: ArrayList<ItemsViewModelRecenzioni>) : RecyclerView.Adapter<RecenzioniAdapter.ViewHolder>(){
    private var onClickListener: RecenzioniAdapter.OnClickListener?= null
    private var buffer= Buffer()
    class ViewHolder(binding: CardRecenzioniBinding):RecyclerView.ViewHolder(binding.root){
        val nome = binding.textViewAuthorName
        val descrizione = binding.textViewReviewText
        val ratingbar = binding.searchFragmentRatingBar
        val btnDelete = binding.RecenzioneBtnDelete
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
        var utente=buffer.getUtente()
        if (utente != null) {
            if(utente.id.compareTo(ItemsViewModelRecenzioni.id_recenzioni)==0){
                holder.btnDelete.visibility= View.VISIBLE
            }else{
                holder.btnDelete.visibility= View.GONE
            }
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
