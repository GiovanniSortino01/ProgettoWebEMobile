package com.example.progettowebemobile.principale.home.pagamenti

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.text.toSpanned
import androidx.recyclerview.widget.RecyclerView
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.R
import com.example.progettowebemobile.Utils
import com.example.progettowebemobile.databinding.AccountItemBinding
import com.example.progettowebemobile.databinding.ItemPagamentiBinding
import com.example.progettowebemobile.principale.account.ItemsViewModelPost
import com.example.progettowebemobile.principale.search.RecyclerView.AccountAdapter
import com.example.progettowebemobile.principale.search.RecyclerView.ItemsViewModelAccount
import com.example.progettowebemobile.principale.search.RecyclerView.Place.RecenzioniAdapter
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

    class CardAdapter(private val mList: ArrayList<ItemViewModelCard>, private val context: Context) : RecyclerView.Adapter<CardAdapter.ViewHolder>(){
    private var onClickListener: CardAdapter.OnClickListener? = null
    private var utils= Utils()

    class ViewHolder(binding: ItemPagamentiBinding): RecyclerView.ViewHolder(binding.root){
        val titolare = binding.pagamentiItemTvTitolare
        val numero = binding.pagamentiItemTvNum
        val scadenza = binding.pagamentiItemTvScadenza
        val immagine = binding.pagamentiItemImage

        val btnDelete = binding.floatingActionButton
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAdapter.ViewHolder {
        val view = ItemPagamentiBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return CardAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardAdapter.ViewHolder, position: Int) {
        val ItemViewModelCard = mList[position]
        var num = ItemViewModelCard.numero
        val modifiedString = StringBuilder(num)
        modifiedString.replace(0, num.length - 4, "**** **** **** ")
        holder.numero.text=modifiedString.toString()
        holder.titolare.text=ItemViewModelCard.nome
        holder.immagine.setImageResource(R.drawable.cartacredito)
        holder.scadenza.text = ItemViewModelCard.data_scadenza
        holder.btnDelete.setOnClickListener{
            showDialogToConfirmExit(ItemViewModelCard.numero, position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ItemViewModelCard)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    private fun showDialogToConfirmExit(id: String, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.Place_RecenzioneConfirm_title))
        builder.setMessage(context.getString(R.string.Place_RecenzioneConfirm_text))
        builder.setPositiveButton(context.getString(R.string.Place_RecenzioneConfirm_yes)) { dialog, which ->
            delete(id, position)
            dialog.dismiss()
        }
        builder.setNegativeButton(context.getString(R.string.Place_RecenzioneConfirm_no)) { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun delete(id: String, position: Int) {
        val query = "DELETE FROM carte WHERE numero_carta = '$id'"
        Log.i("LOG", "Query creata: $query")


        ClientNetwork.retrofit.remove(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.i("LOG", "Query creata: $query")
                if (response.isSuccessful) {
                    utils.PopError(
                        context.getString(R.string.Place_RecenzioneConfirm2_title),
                        context.getString(R.string.Place_RecenzioneConfirm2_text),
                        context
                    )
                    mList.removeAt(position) // Rimuovi l'elemento dalla lista
                    notifyItemRemoved(position) // Notifica l'adapter della rimozione dell'elemento
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