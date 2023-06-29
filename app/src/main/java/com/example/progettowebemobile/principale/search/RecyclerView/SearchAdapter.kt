package com.example.progettowebemobile.principale.search.RecyclerView

import android.content.ContentValues.TAG
import android.content.Context
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
import com.example.progettowebemobile.databinding.SearchItemBinding
import com.example.progettowebemobile.entity.Luogo
import com.example.progettowebemobile.entity.Utente
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchAdapter(private val mList: List<ItemsViewModelSearch>,private val context: Context) : RecyclerView.Adapter<SearchAdapter.ViewHolder>(){
    private var itemClickListener: ((Object) -> Unit)? = null
    private var onClickListener: OnClickListener?= null
    private var buffer = Buffer()


    class ViewHolder(binding: SearchItemBinding):RecyclerView.ViewHolder(binding.root){
        val imageView = binding.searchImageView
        val name = binding.searchNameTextView
        val luogo = binding.searchLocationTextView
        val recenzioni = binding.searchRatingBar
        val preferito = binding.searchFavoriteButton
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val view = SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        Log.i(TAG,"$mList")
        val ItemsViewModelSearch = mList[position]
        holder.imageView.setImageBitmap(ItemsViewModelSearch.image)
        holder.name.text=ItemsViewModelSearch.name
        holder.luogo.text=ItemsViewModelSearch.luogo
        holder.recenzioni.rating=ItemsViewModelSearch.recenzione
        var item = buffer.getUtente()
        var id_utente = item?.id
        var id_luogo=ItemsViewModelSearch.id
        var preferito = ItemsViewModelSearch.preferito
        if(id_utente!=null) {
            preferito(id_utente, id_luogo) { pref ->
                preferito = pref
                if(preferito){
                    holder.preferito.setImageResource(R.drawable.baseline_favorite_24)
                }else{
                    holder.preferito.setImageResource(R.drawable.baseline_favorite_border_24)
                }
            }
        }

        if(preferito){
            holder.preferito.setImageResource(R.drawable.baseline_favorite_24)
        }else{
            holder.preferito.setImageResource(R.drawable.baseline_favorite_border_24)
        }

        holder.preferito.setOnClickListener {
            if (id_utente != null) {
                if (preferito) {
                    Log.i(TAG,"$preferito")
                    delete(id_luogo, id_utente, mList[position],position)
                } else {
                    Log.i(TAG,"$preferito")
                    insert(id_luogo, id_utente, mList[position],position)
                }
            }
        }

        holder.itemView.setOnClickListener{
            onClickListener?.onClick(position,ItemsViewModelSearch)
            getItem(id_luogo,preferito)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnClickListener{
        fun onClick(position:Int, model:ItemsViewModelSearch)
    }

    fun setOnClickListener(onClickListener:OnClickListener){
        this.onClickListener =  onClickListener
    }
    fun setOnItemClickListener(listener: (Object) -> Unit) {
        itemClickListener = listener
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
                            var utente:Utente? = buffer.getUtente()
                            bundle.putSerializable("utente", utente)
                            val navController = Navigation.findNavController(context as AppCompatActivity, R.id.fragmentPrincipale)
                            navController.navigate(R.id.action_reciclerViewSearch_to_placeFragment,bundle)

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


    private fun insert(id_luogo: Int,id_utente: Int,ItemsViewModelSearch: ItemsViewModelSearch,adapterPosition:Int) {
        val query = "INSERT INTO preferiti (id_persona, id_luogo) VALUES ('$id_utente', '$id_luogo');"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {


                override fun onResponse(

                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server
                    Log.i("LOG", "Query creata:$query ")
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    if (response.isSuccessful) { //Se non ci sono stati errori di connessione con il server
                        ItemsViewModelSearch.preferito=true
                        notifyItemChanged(adapterPosition)
                    } else {
                        ItemsViewModelSearch.preferito=true
                        Log.i("LOG", "Errore durante la registrazione ")
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>,
                    t: Throwable
                ) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                    Log.i("LOG", "Errore durante l'inserimento")
                    ItemsViewModelSearch.preferito=true
                    //utils.PopError(getString(R.string.login_db_error_title), getString(R.string.login_db_error),this@Registrazione)
                }
            }
        )
    }

    private fun delete(id_luogo: Int, id_utente: Int, ItemsViewModelSearch: ItemsViewModelSearch, adapterPosition: Int) {
        val query = "DELETE FROM preferiti WHERE id_luogo = '$id_luogo' and id_persona = '$id_utente'"
        Log.i("LOG", "Query creata: $query")

        ClientNetwork.retrofit.remove(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.i("LOG", "Query creata: $query")
                Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                if (response.isSuccessful) {
                    // Aggiorna l'immagine dell'elemento preferito nell'adapter
                    ItemsViewModelSearch.preferito=false
                    notifyItemChanged(adapterPosition)
                } else {
                    Log.i("LOG", "Errore durante la cancellazione")
                    ItemsViewModelSearch.preferito = false
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.i("LOG", "Errore durante la cancellazione")
                ItemsViewModelSearch.preferito = false
                // Gestisci l'errore di connessione o visualizza un messaggio di errore appropriato.
            }
        })
    }
    fun preferito(id_utente:Int,id_luogo:Int,callback: (Boolean) -> Unit){
        val query2 = "select * from preferiti where id_persona = '$id_utente' and id_luogo = '$id_luogo'"
        Log.i("LOG", "Query creata:$query2 ")

        ClientNetwork.retrofit.login(query2).enqueue(
            object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    if (response.isSuccessful) {
                        val queryset = response.body()?.getAsJsonArray("queryset")
                        if (queryset?.size()!! >= 1) {
                            callback(true)
                        } else {
                            callback(false)
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                    //Toast.makeText( this@MainActivity,"onFailure1", Toast.LENGTH_SHORT).show()
                    Log.i("onFailure", "Sono dentro al onFailure")
                    callback(false)                }
            }
        )
    }
}
