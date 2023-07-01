package com.example.progettowebemobile.principale.search.RecyclerView.Place

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.CardRecenzioniBinding
import com.example.progettowebemobile.principale.account.ItemsViewModelPost
import com.example.progettowebemobile.principale.account.PersonalAccountAdapter
import com.example.progettowebemobile.principale.search.RecyclerView.ItemsViewModelSearch
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.Utils
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class RecenzioniAdapter (private val mList: ArrayList<ItemsViewModelRecenzioni>, private val context: Context) : RecyclerView.Adapter<RecenzioniAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var buffer=Buffer()
    private var utils = Utils()

    class ViewHolder(binding: CardRecenzioniBinding): RecyclerView.ViewHolder(binding.root) {
        val nome = binding.textViewAuthorName
        val descrizione = binding.textViewReviewText
        val ratingbar = binding.searchFragmentRatingBar
        val btnDelete = binding.reviewsBtnDelete
        val immagine = binding.reviewsImageUser
        val data_pubblicazione = binding.textViewDate
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecenzioniAdapter.ViewHolder {
        val view = CardRecenzioniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecenzioniAdapter.ViewHolder, position: Int) {
        val itemsViewModelRecenzioni = mList[position]
        holder.nome.text = itemsViewModelRecenzioni.nome
        holder.descrizione.text = itemsViewModelRecenzioni.descriziona
        holder.ratingbar.rating = itemsViewModelRecenzioni.recenzioni
        holder.data_pubblicazione.text = itemsViewModelRecenzioni.data_pubblicazione
        getImage(itemsViewModelRecenzioni.foto){
            data -> holder.immagine.setImageBitmap(data)
        }

        var a = itemsViewModelRecenzioni.recenzioni
        Log.i(TAG,"$a ")
        holder.nome.setOnClickListener{
            //spostati nella home dell'utente
        }
        var utente=buffer.getUtente()
        if (utente != null) {
            if(utente.id.compareTo(itemsViewModelRecenzioni.id_utente)==0){
                holder.btnDelete.visibility= View.VISIBLE
            }else{
                holder.btnDelete.visibility= View.GONE
            }
        }
        holder.btnDelete.setOnClickListener {
            showDialogToConfirmExit(itemsViewModelRecenzioni.id_recenzioni, position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ItemsViewModelRecenzioni)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    private fun showDialogToConfirmExit(id: Int, position: Int) {
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

    private fun delete(id: Int, position: Int) {
        val query = "DELETE FROM recenzioni WHERE id_recenzione = '$id'"
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
    private fun getImage(url: String, callback: (Bitmap?) -> Unit) {
        ClientNetwork.retrofit.getAvatar(url).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val bitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
                    callback(bitmap)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback(null)
            }
        })
    }
}

