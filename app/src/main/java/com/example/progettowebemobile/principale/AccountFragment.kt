package com.example.progettowebemobile.principale

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.entity.Utente
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.R
import com.example.progettowebemobile.Utils
import com.example.progettowebemobile.databinding.FragmentAccountBinding
import com.example.progettowebemobile.principale.account.ItemsViewModelPost
import com.example.progettowebemobile.principale.account.PersonalAccountAdapter
import com.example.progettowebemobile.principale.search.RecyclerView.AccountAdapter
import com.example.progettowebemobile.principale.search.RecyclerView.ItemsViewModelAccount
import com.example.progettowebemobile.principale.search.RecyclerView.ItemsViewModelSearch
import com.example.progettowebemobile.principale.search.RecyclerView.SearchAdapter
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var utente: Utente
    private var utils= Utils()
    private var buffer = Buffer()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val intent = Intent()
        utente = buffer.getUtente()!!

        binding = FragmentAccountBinding.inflate(inflater, container, false)

        binding.userRecycleView.layoutManager = LinearLayoutManager(requireContext())

        val nomeUtente = utente.nome
        val cognomeUtente = utente.cognome
        val dataInscrizioneUtente = utente?.data

        binding.userName.text = nomeUtente
        binding.userSurname.text = cognomeUtente
        binding.userRegistrationDate.text = dataInscrizioneUtente
        getImageProfilo(utente.immagine)

        loadRecyclerViewData()
        binding.userBtnEdit.setOnClickListener{
            val inflater = LayoutInflater.from(context)
            val popupView = inflater.inflate(R.layout.pop_up_edit, null)
            val Et_nome = popupView.findViewById<EditText>(R.id.user_edit_etName)
            val Et_cognome = popupView.findViewById<EditText>(R.id.user_edit_etSurname)
            val Et_email = popupView.findViewById<EditText>(R.id.user_edit_etEmail)
            val alertDialogBuilder = AlertDialog.Builder(context).setView(popupView)
            val alertDialog = alertDialogBuilder.create()

        }

        binding.btnAddPost.setOnClickListener{
            //pop-up
            val inflater = LayoutInflater.from(context)
            val popupView = inflater.inflate(R.layout.pop_up_post, null)
            val popupButtonClose = popupView.findViewById<Button>(R.id.close_button)
            val popupButtonAdd = popupView.findViewById<Button>(R.id.add_botton)
            val Et_descrizione = popupView.findViewById<EditText>(R.id.Et_descrizione)
            val Et_luogo = popupView.findViewById<EditText>(R.id.Et_luogo)
            val alertDialogBuilder = AlertDialog.Builder(context).setView(popupView)
            val alertDialog = alertDialogBuilder.create()
            popupButtonClose.setOnClickListener {
                alertDialog.dismiss()
            }
            popupButtonAdd.setOnClickListener {
                var descrizione = Et_descrizione.text.toString()
                var luogo = Et_luogo.text.toString()
                if(descrizione.isEmpty() || luogo.isEmpty()){
                    utils.PopError(getString(R.string.user_error_popup_title),getString(R.string.user_error_popup_text),requireContext())
                }else {
                    insert(utente.id, descrizione, luogo)
                    alertDialog.dismiss()
                }
            }
            val builder = AlertDialog.Builder(context)
            builder.setView(popupView)

            //visualizza il pop-up
            alertDialog.show()
        }

        return binding.root
    }

    private fun loadRecyclerViewData() {
            getItems(utente.id) { data ->
                val adapter = PersonalAccountAdapter(data)
                binding.userRecycleView.adapter = adapter

                adapter.setOnClickListener(object : PersonalAccountAdapter.OnClickListener {
                    override fun onClick(position: Int, model: ItemsViewModelPost) {
                        Log.i(ContentValues.TAG, "Index ${position + 1}")
                    }
                })
                adapter.notifyDataSetChanged() // Aggiungi questa linea per aggiornare l'adapter
            }
    }
    private fun getImageProfilo(url: String){
        ClientNetwork.retrofit.getAvatar(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {
                        var avatar: Bitmap? = null
                        if (response.body()!=null) {
                            avatar = BitmapFactory.decodeStream(response.body()?.byteStream())
                            binding.imageView2.setImageBitmap(avatar)
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                }
            }
        )
    }
    private fun getItems(
        id: Int,
        callback: (ArrayList<ItemsViewModelPost>) -> Unit // Callback per restituire il risultato nullable
    ) {
        val query = "select * from post where id_persona = '$id';"
        val data = ArrayList<ItemsViewModelPost>()

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {

                        var completedCount = 0 // Contatore per tenere traccia del numero di chiamate completate

                        for (i in 0 until queryset.size()) {
                            val item = queryset.get(i).asJsonObject
                            var foto=item.get("foto").asString
                            getImageProfiloRecyclerView(foto) { avatar ->
                                completedCount++
                                if (avatar != null) {
                                    data.add(ItemsViewModelPost(item.get("id_post").asInt ,utente.nome,utente.cognome,avatar, item.get("descrizione").asString, item.get("luogo").asString, item.get("data").asString))
                                }
                                // Verifica se tutte le chiamate sono state completate
                                if (completedCount == queryset.size()) {
                                    callback(data)
                                }
                            }
                        }
                    } else {
                        callback(data)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Chiamata alla callback con valore null in caso di fallimento
                    callback(data)
                }
            }
        )
    }
    private fun getImageProfiloRecyclerView(url: String, callback: (Bitmap?) -> Unit) {
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
    fun insert(id:Int,descrizione:String,luogo:String) {

        val currentDate = LocalDate.now()
        val query =
            "INSERT INTO post (id_persona, descrizione , luogo, data) VALUES ('${id}', '${descrizione}', '${luogo}', '$currentDate');"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {


                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server

                    Log.i("LOG", "Query creata:$query ")
                    if (response.isSuccessful) { //Se non ci sono stati errori di connessione con il server
                        // utils.PopError(getString(R.string.register_new_account_title),getString(R.string.register_new_account_title),this@Registrazione)

                    } else {
                        Log.i("LOG", "Errore durante la registrazione ")
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>,
                    t: Throwable
                ) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.

                    //utils.PopError(getString(R.string.login_db_error_title), getString(R.string.login_db_error),this@Registrazione)
                }
            }
        )
    }
}
