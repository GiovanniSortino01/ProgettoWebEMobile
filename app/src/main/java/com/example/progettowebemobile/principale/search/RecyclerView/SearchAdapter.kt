package com.example.progettowebemobile.principale.search.RecyclerView

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
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchAdapter.ViewHolder {
        val view = SearchItemBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchAdapter.ViewHolder, position: Int) {
        val ItemsViewModelSearch = mList[position]
        holder.imageView.setImageBitmap(ItemsViewModelSearch.image)
        holder.name.text=ItemsViewModelSearch.name
        holder.luogo.text=ItemsViewModelSearch.luogo
        holder.recenzioni.rating=ItemsViewModelSearch.recenzione
        holder.itemView.setOnClickListener{
            onClickListener?.onClick(position,ItemsViewModelSearch)
            var nome2=ItemsViewModelSearch.name
            var luogo2=ItemsViewModelSearch.luogo
            getItem(nome2,luogo2)
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

    private fun getItem (nome:String, luogo:String){

        val query = "select * from luoghi where nome = '${nome}' and luogo = '${luogo}';"
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
                            var luogo = Luogo(id_luogo,nome,descrizione,numero_di_cellulare,indirizzo,foto,valutazione,luogoposto,tipo,sitoweb,come_arrivarci)
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

}