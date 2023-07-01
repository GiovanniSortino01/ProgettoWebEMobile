package com.example.progettowebemobile.principale.home.preferiti

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.databinding.FragmentPreferitiBinding
import com.example.progettowebemobile.principale.search.RecyclerView.Place.ItemsViewModelRecenzioni
import com.example.progettowebemobile.principale.search.RecyclerView.Place.RecenzioniAdapter
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PreferitiFragment : Fragment() {
    private var item = Buffer()
    private lateinit var binding: FragmentPreferitiBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentPreferitiBinding.inflate(inflater, container, false)
        var utente = item.getUtente()
        var id = utente?.id
        if(id!=null) {
            loadRecyclerViewData(id)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())



        return binding.root
    }

    private fun loadRecyclerViewData(id:Int) {
        getItems(id) { data ->
            val adapter = PreferitiAdapter(data,requireContext())
            binding.recyclerView.adapter = adapter
            adapter.setOnClickListener(object : PreferitiAdapter.OnClickListener {
                override fun onClick(position: Int, model: ItemViewModelPreferiti) {
                    Log.i(ContentValues.TAG,"Index ${position + 1}")
                }
            })
            adapter.notifyDataSetChanged() // Aggiungi questa linea per aggiornare l'adapter
        }
    }

    private fun getItems(id: Int, callback: (ArrayList<ItemViewModelPreferiti>) -> Unit) {
        val query = "SELECT * FROM preferiti p,luoghi l WHERE p.id_persona = '$id' and l.id_luogo=p.id_luogo"
        val data = ArrayList<ItemViewModelPreferiti>()

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {

                        var completedCount = 0 // Contatore per tenere traccia del numero di chiamate completate

                        for (i in 0 until queryset.size()) {
                            val item = queryset.get(i).asJsonObject
                            var id_luogo = item.get("id_luogo").asInt
                            var recenzioni = item.get("valutazione").asFloat
                            var nome_luogo = item.get("luogo").asString
                            var immagine = item.get("fotoPrincipale").asString
                            var nome = item.get("nome").asString
                            if(immagine!=null){
                                data.add(
                                    ItemViewModelPreferiti(
                                        id_luogo,
                                        nome_luogo,
                                        nome,
                                        recenzioni,
                                        immagine,
                                    )
                                )
                            }

                            // Incrementa completedCount dopo aver aggiunto un elemento a data
                            completedCount++

                            // Verifica se tutte le chiamate sono state completate
                            if (completedCount == queryset.size()) {
                                callback(data)
                            }
                        }

                    }
                    else {
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
}