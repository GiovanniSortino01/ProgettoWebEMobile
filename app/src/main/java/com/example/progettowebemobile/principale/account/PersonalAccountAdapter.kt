package com.example.progettowebemobile.principale.account

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.PostItemBinding
import com.example.progettowebemobile.principale.AccountFragment
import com.example.progettowebemobile.principale.search.RecyclerView.Place.ItemsViewModelRecenzioni
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalAccountAdapter(private val mList: ArrayList<ItemsViewModelPost>,val context: Context,val account:AccountFragment?) : RecyclerView.Adapter<PersonalAccountAdapter.ViewHolder>(){

    private var onClickListener: OnClickListener?= null

    class ViewHolder(binding: PostItemBinding): RecyclerView.ViewHolder(binding.root){
        val btn = binding.btnDelete
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

        if(account==null){
            holder.btn.visibility= View.GONE
        }

        holder.btn.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage("Sei sicuro di voler eliminare il post?")
                .setTitle("Eliminazione Post")
                .setPositiveButton(R.string.dialog_concedi) { dialog, _ ->
                    dialog.dismiss()
                    delete(ItemsViewModelPost.id)
                }
                .setNegativeButton(R.string.dialog_annulla) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
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
    private fun delete(id: Int) {
        val query = "DELETE FROM post WHERE id_post = '$id'"
        Log.i("LOG", "Query creata: $query")


        ClientNetwork.retrofit.remove(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.i("LOG", "Query creata: $query")
                if (response.isSuccessful) {
                    account?.loadRecyclerViewData()
                } else {
                    Log.i("LOG", "Errore durante la cancellazione")
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                // Gestisci l'errore di connessione o visualizza un messaggio di errore appropriato.
            }
        })
    }
}