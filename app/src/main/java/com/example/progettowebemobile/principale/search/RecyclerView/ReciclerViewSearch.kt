package com.example.progettowebemobile.principale.search.RecyclerView

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.R
import com.example.progettowebemobile.Utils
import com.example.progettowebemobile.databinding.FragmentRecyclerviewSearchBinding
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReciclerViewSearch : Fragment() {
    private var objectList = emptyList<Object>()

    private lateinit var binding: FragmentRecyclerviewSearchBinding
    private lateinit var utils: Utils
    private var tipo: String =""
    private lateinit var avatar: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecyclerviewSearchBinding.inflate(inflater, container, false)


        binding.searchRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        setFragmentResultListener("requestKey") { requestKey, bundle ->
            tipo = bundle.getString("bundleKey").toString()

            // Chiamata alla funzione per caricare i dati nella RecyclerView
            loadRecyclerViewData()
        }

        return binding.root
    }

    private fun loadRecyclerViewData() {
        if (tipo.equals("ristorante") || tipo.equals("hotel") || tipo.equals("monumento")) {
            getItems(tipo) { data ->
                val adapter = SearchAdapter(data,requireActivity().supportFragmentManager)
                binding.searchRecyclerView.adapter = adapter

                adapter.setOnClickListener(object : SearchAdapter.OnClickListener {
                    override fun onClick(position: Int, model: ItemsViewModelSearch) {
                        Log.i(TAG, "Index ${position + 1}")
                    }
                })
                adapter.notifyDataSetChanged() // Aggiungi questa linea per aggiornare l'adapter
            }
        }else if(tipo.equals("persona")){
            getPersone() { data ->
                val adapter = AccountAdapter(data,requireActivity().supportFragmentManager)
                binding.searchRecyclerView.adapter = adapter

                adapter.setOnClickListener(object : AccountAdapter.OnClickListener {
                    override fun onClick(position: Int, model: ItemsViewModelAccount) {
                        Log.i(TAG, "Index ${position + 1}")
                    }
                })
                adapter.notifyDataSetChanged() // Aggiungi questa linea per aggiornare l'adapter
            }
        }
        else {
            Log.i(TAG, "Tipo non valido")
        }
    }

    private fun getItems(
        tipo: String,
        callback: (ArrayList<ItemsViewModelSearch>) -> Unit // Callback per restituire il risultato nullable
    ) {
        val query = "select * from luoghi where tipo = '$tipo';"
        val data = ArrayList<ItemsViewModelSearch>()

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {

                        var completedCount = 0 // Contatore per tenere traccia del numero di chiamate completate

                        for (i in 0 until queryset.size()) {
                            val item = queryset.get(i).asJsonObject
                            getImageProfilo(item.get("fotoPrincipale").asString) { avatar ->
                                completedCount++
                                if (avatar != null) {
                                    data.add(ItemsViewModelSearch(avatar, item.get("nome").asString, item.get("luogo").asString, item.get("valutazione").asInt))
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

    private fun getImageProfilo(url: String, callback: (Bitmap?) -> Unit) {
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
        private fun getPersone (callback: (ArrayList<ItemsViewModelAccount>) -> Unit): ArrayList<ItemsViewModelAccount>{

            val query = "select * from utenti;"
            Log.i("LOG", "Query creata:$query ")
            val data = ArrayList<ItemsViewModelAccount>()

            ClientNetwork.retrofit.login(query).enqueue(
                object : Callback<JsonObject> {

                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server
                        Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                        if (response.isSuccessful) {
                            val queryset = response.body()?.getAsJsonArray("queryset")
                            if (queryset?.size()!! >= 1) {
                                var completedCount = 0 // Contatore per tenere traccia del numero di chiamate completate

                                for (i in 0 until queryset.size()) {
                                    val item = queryset.get(i).asJsonObject
                                    getImageProfilo(item.get("immagine").asString) { avatar ->
                                        completedCount++
                                        if (avatar != null) {
                                            data.add(ItemsViewModelAccount(item.get("id").asInt,avatar, item.get("nome").asString, item.get("cognome").asString, item.get("datainscrizione").asString))
                                        }
                                        // Verifica se tutte le chiamate sono state completate
                                        if (completedCount == queryset.size()) {
                                            callback(data)
                                        }
                                    }
                                }
                            } else {
                                //Non ci sono oggetti da aggiungere alla recyclerView
                            }
                        }
                    }
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                        //Toast.makeText( this@MainActivity,"onFailure1", Toast.LENGTH_SHORT).show()
                        Log.i("onFailure", "Sono dentro al onFailure")
                        utils.PopError(getString(R.string.login_db_error_title), getString(R.string.login_db_error),requireContext())
                    }
                }
            )
            return data
        }
}
