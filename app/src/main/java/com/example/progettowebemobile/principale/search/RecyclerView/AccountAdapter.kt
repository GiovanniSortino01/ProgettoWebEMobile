package com.example.progettowebemobile.principale.search.RecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.AccountItemBinding
import com.example.progettowebemobile.principale.search.RecyclerView.Place.PlaceFragment


class AccountAdapter(private val mList: List<ItemsViewModelAccount>,private val context: Context) : RecyclerView.Adapter<AccountAdapter.ViewHolder>(){
    private var itemClickListener = emptyList<Object>()

    private var onClickListener: OnClickListener?= null
    private var lastClickTime: Long = 0
    private val DOUBLE_CLICK_TIME_DELTA: Long = 300 // Tempo di doppio clic desiderato in millisecondi


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
            val navController = Navigation.findNavController(context as AppCompatActivity, R.id.fragmentPrincipale)
            navController.navigate(R.id.action_reciclerViewSearch_to_placeFragment)


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