package com.example.progettowebemobile.principale.search.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.databinding.AccountItemBinding

class AccountAdapter(private val mList: List<ItemsViewModelAccount>) : RecyclerView.Adapter<AccountAdapter.ViewHolder>(){

    private var onClickListener: OnClickListener?= null

    class ViewHolder(binding: AccountItemBinding): RecyclerView.ViewHolder(binding.root){
        val imageView = binding.imageViewProfile
        val name = binding.textViewFullName
        val data_di_inscrizione = binding.textViewDate

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountAdapter.ViewHolder {
        val view = AccountItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountAdapter.ViewHolder, position: Int) {
        val ItemsViewModelAccount = mList[position]
        holder.imageView.setImageBitmap(ItemsViewModelAccount.image)
        holder.name.text=ItemsViewModelAccount.name + " " +ItemsViewModelAccount.cognome
        holder.data_di_inscrizione.text=ItemsViewModelAccount.data_di_inscrizione
        holder.itemView.setOnClickListener{
            onClickListener?.onClick(position,ItemsViewModelAccount)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnClickListener{
        fun onClick(position:Int, model:ItemsViewModelAccount)
    }

    fun setOnClickListener(onClickListener:OnClickListener){
        this.onClickListener =  onClickListener
    }

}