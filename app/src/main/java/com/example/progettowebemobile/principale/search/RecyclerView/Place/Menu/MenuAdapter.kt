package com.example.progettowebemobile.principale.search.RecyclerView.Place.Menu

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.Utils
import com.example.progettowebemobile.databinding.CardRecenzioniBinding
import com.example.progettowebemobile.databinding.FragmentMenuBinding
import com.example.progettowebemobile.databinding.ItemMenuBinding
import com.example.progettowebemobile.principale.account.ItemsViewModelPost
import com.example.progettowebemobile.principale.search.RecyclerView.Place.ItemsViewModelRecenzioni

class MenuAdapter (private val mList: ArrayList<ItemsViewModelMenu>, private val context: Context) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    private var onClickListener: MenuAdapter.OnClickListener? = null
    private var utils = Utils()

    class ViewHolder(binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        val nome = binding.menuNomePortata
        val ingredienti = binding.menuIngredienti
        val prezzo = binding.menuPrezzo
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.ViewHolder {
        val view = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuAdapter.ViewHolder, position: Int) {
        val itemsViewModelMenu = mList[position]
        holder.nome.text = itemsViewModelMenu.nome
        holder.ingredienti.text = itemsViewModelMenu.ingredienti
        holder.prezzo.text = itemsViewModelMenu.prezzo.toString()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ItemsViewModelMenu)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
}