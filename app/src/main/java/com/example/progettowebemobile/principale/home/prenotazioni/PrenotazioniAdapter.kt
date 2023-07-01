package com.example.progettowebemobile.principale.home.prenotazioni

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.R
import com.example.progettowebemobile.Utils
import com.example.progettowebemobile.databinding.ItemPreferitiBinding
import com.example.progettowebemobile.databinding.ItemPrenotazioniBinding
import com.example.progettowebemobile.entity.Luogo
import com.example.progettowebemobile.entity.Utente
import com.example.progettowebemobile.principale.home.preferiti.ItemViewModelPreferiti
import com.example.progettowebemobile.principale.home.preferiti.PreferitiAdapter
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrenotazioniAdapter  (private val mList: ArrayList<ItemsViewModelPrenotazioni>, private val context: Context) : RecyclerView.Adapter<PrenotazioniAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var item = Buffer()
    private var utils = Utils()

    class ViewHolder(binding: ItemPrenotazioniBinding) : RecyclerView.ViewHolder(binding.root) {
        val foto = binding.preferitiImmagine
        val nome_luogo = binding.prenotazioniPosto
        val data_prenotazione = binding.prenotazioniData
        val prezzo = binding.prenotazioniCosto
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PrenotazioniAdapter.ViewHolder {
        val view =
            ItemPrenotazioniBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrenotazioniAdapter.ViewHolder, position: Int) {
        val ItemsViewModelPrenotazioni = mList[position]
        holder.nome_luogo.text = ItemsViewModelPrenotazioni.nome_luogo
        holder.data_prenotazione.text = ItemsViewModelPrenotazioni.data_prenotazione
        holder.prezzo.text = ItemsViewModelPrenotazioni.prezzo
        getImage(ItemsViewModelPrenotazioni.foto) { foto ->
            holder.foto.setImageBitmap(foto)
        }
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, ItemsViewModelPrenotazioni)
            getItem(ItemsViewModelPrenotazioni.id_luogo, true)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ItemsViewModelPrenotazioni)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
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
    private fun getItem (id:Int,preferito:Boolean){

        val query = "select * from luoghi where id_luogo = '$id';"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    if (response.isSuccessful) {
                        val queryset = response.body()?.getAsJsonArray("queryset")
                        if (queryset?.size() == 1) {
                            val utenteJsonObject = queryset.get(0).asJsonObject
                            val id_luogo = utenteJsonObject.get("id_luogo").asInt
                            val nome = utenteJsonObject.get("nome").asString
                            val descrizione = utenteJsonObject.get("descrizione").asString
                            val numero_di_cellulare = utenteJsonObject.get("numero_di_cellulare").asLong
                            val indirizzo = utenteJsonObject.get("indirizzo").asString
                            val valutazione = utenteJsonObject.get("valutazione").asFloat
                            val foto = utenteJsonObject.get("foto").asString
                            val luogoposto = utenteJsonObject.get("luogo").asString
                            val sitoweb = utenteJsonObject.get("sitoweb").asString
                            val tipo = utenteJsonObject.get("tipo").asString
                            val come_arrivarci = utenteJsonObject.get("comearrivarci").asString
                            var luogo = Luogo(id_luogo,nome,descrizione,numero_di_cellulare,indirizzo,foto,valutazione,luogoposto,tipo,sitoweb,come_arrivarci,preferito)
                            val bundle = Bundle()
                            bundle.putSerializable("itemViewModel", luogo) // Passa l'oggetto ItemsViewModelSearch come serializzabile
                            var utente: Utente? = item.getUtente()
                            bundle.putSerializable("utente", utente)
                            val navController = Navigation.findNavController(context as AppCompatActivity, R.id.fragmentPrincipale)
                            navController.navigate(R.id.action_prenotazioniFragment2_to_placeFragment,bundle)

                        } else {
                            // utils.PopError(getString(R.string.login_credenziali_errate_titolo), getString(R.string.login_credenziali_errate),this@Login)
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                    //Toast.makeText(this@MainActivity,"onFailure1", Toast.LENGTH_SHORT).show()
                    Log.i("onFailure", "Sono dentro al onFailure")
                    // utils.PopError(getString(R.string.login_db_error_title), getString(R.string.login_db_error),this@Login)
                }
            }
        )
    }
}