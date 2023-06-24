package com.example.progettowebemobile.principale.search.RecyclerView

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.AccountItemBinding
import com.example.progettowebemobile.entity.Luogo
import com.example.progettowebemobile.entity.Persona
import com.example.progettowebemobile.principale.search.RecyclerView.Place.PlaceFragment
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
            var id=ItemsViewModelAccount.id
            getItem(id)
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
    private fun getItem (id:Int){

        val query = "select * from utenti where id = '${id}';"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    if (response.isSuccessful) {
                        val queryset = response.body()?.getAsJsonArray("queryset")
                        if (queryset?.size() == 1) {
                            val utenteJsonObject = queryset.get(0).asJsonObject
                            val id = utenteJsonObject.get("id").asInt
                            val nome = utenteJsonObject.get("nome").asString
                            val cognome = utenteJsonObject.get("cognome").asString
                            val data = utenteJsonObject.get("data").asString
                            val immagine = utenteJsonObject.get("immagine").asString
                            var persona = Persona(id,nome,cognome,data,immagine)
                            val bundle = Bundle()
                            bundle.putSerializable("itemViewModel", persona) // Passa l'oggetto ItemsViewModelSearch come serializzabile

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