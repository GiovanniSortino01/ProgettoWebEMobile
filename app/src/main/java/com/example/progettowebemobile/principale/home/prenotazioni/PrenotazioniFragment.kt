package com.example.progettowebemobile.principale.home.prenotazioni

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
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.FragmentPlaceBinding
import com.example.progettowebemobile.databinding.FragmentPrenotazioniBinding
import com.example.progettowebemobile.principale.home.preferiti.ItemViewModelPreferiti
import com.example.progettowebemobile.principale.home.preferiti.PreferitiAdapter
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrenotazioniFragment : Fragment() {
    private var item = Buffer()
    private lateinit var binding: FragmentPrenotazioniBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPrenotazioniBinding.inflate(inflater, container, false)

        var utente = item.getUtente()
        var id = utente?.id
        if(id!=null) {
            loadRecyclerViewData(id)
        }
        binding.prenotazioniRv.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    private fun loadRecyclerViewData(id:Int) {
        getItems(id) { data ->
            val adapter = PrenotazioniAdapter(data,requireContext())
            binding.prenotazioniRv.adapter = adapter
            adapter.setOnClickListener(object : PrenotazioniAdapter.OnClickListener {
                override fun onClick(position: Int, model: ItemsViewModelPrenotazioni) {
                    Log.i(ContentValues.TAG,"Index ${position + 1}")
                }
            })
            adapter.notifyDataSetChanged() // Aggiungi questa linea per aggiornare l'adapter
        }
    }
    private fun getItems(id: Int, callback: (ArrayList<ItemsViewModelPrenotazioni>) -> Unit) {
        val query = "SELECT * FROM prenotazioni p,luoghi l WHERE p.id_persona = '$id' and l.id_luogo=p.id_luogo"
        val data = ArrayList<ItemsViewModelPrenotazioni>()

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {

                        var completedCount = 0 // Contatore per tenere traccia del numero di chiamate completate

                        for (i in 0 until queryset.size()) {
                            val item = queryset.get(i).asJsonObject
                            var id_luogo = item.get("id_luogo").asInt
                            var nome_luogo = item.get("nome_luogo").asString
                            var data_prenotazione = item.get("data").asString
                            var prezzo = item.get("prezzo").asString
                            var foto = item.get("fotoPrincipale").asString
                                data.add(
                                    ItemsViewModelPrenotazioni(
                                        id_luogo,
                                        foto,
                                        nome_luogo,
                                        data_prenotazione,
                                        prezzo
                                    )
                                )


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