package com.example.progettowebemobile.principale.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.databinding.PostItemBinding
import com.example.progettowebemobile.principale.search.RecyclerView.Place.ItemsViewModelRecenzioni

class PersonalAccountAdapter(private val mList: ArrayList<ItemsViewModelPost>) : RecyclerView.Adapter<PersonalAccountAdapter.ViewHolder>(){

    private var onClickListener: OnClickListener?= null

    class ViewHold
    er(binding: PostItemBinding): RecyclerView.ViewHolder(binding.root){
        val foto = binding.photoImageview
        val nome = binding.userNameTextview
        val luogo = binding.tvLuogo
        val descrizione = binding.placeDescriptionTextview
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalAccountAdapter.ViewHolder {
        val view = PostItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonalAccountAdapter.ViewHolder, position: Int) {
        val ItemsViewModelPost = mList[position]
        holder.foto.setImageBitmap(ItemsViewModelPost.image)
        holder.nome.text=ItemsViewModelPost.nome + " " + ItemsViewModelPost.cognome
        holder.luogo.text=ItemsViewModelPost.luogo+ " "+ ItemsViewModelPost.data_caricamento
        holder.descrizione.text=ItemsViewModelPost.descrizione
        holder.itemView.setOnClickListener{
            onClickListener?.onClick(position,ItemsViewModelPost)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnClickListener{
        fun onClick(position:Int, model: ItemsViewModelPost)
    }

    fun setOnClickListener(onClickListener:OnClickListener){
        this.onClickListener =  onClickListener
    }
}