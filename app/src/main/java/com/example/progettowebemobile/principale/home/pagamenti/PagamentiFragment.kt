package com.example.progettowebemobile.principale.home.pagamenti

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
import com.example.progettowebemobile.databinding.FragmentPagamentiBinding
import com.example.progettowebemobile.databinding.FragmentRecyclerviewSearchBinding
import com.example.progettowebemobile.principale.search.RecyclerView.AccountAdapter
import com.example.progettowebemobile.principale.search.RecyclerView.ItemsViewModelAccount
import com.example.progettowebemobile.principale.search.RecyclerView.ItemsViewModelSearch
import com.example.progettowebemobile.principale.search.RecyclerView.SearchAdapter
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PagamentiFragment : Fragment() {

    private lateinit var binding : FragmentPagamentiBinding
    private var buffer= Buffer()
    private var utente = buffer.getUtente()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPagamentiBinding.inflate(inflater, container, false)
        binding.pagamentiRv.layoutManager = LinearLayoutManager(requireContext())
        loadRecyclerViewData()
        return binding.root
    }
    private fun loadRecyclerViewData() {
        utente?.let {
            getItems(it.id) { data ->
                val adapter = CardAdapter(data,requireContext())
                binding.pagamentiRv.adapter = adapter

                adapter.setOnClickListener(object : CardAdapter.OnClickListener {
                    override fun onClick(position: Int, model: ItemViewModelCard) {
                        Log.i(ContentValues.TAG, "Index ${position + 1}")
                    }
                })
                adapter.notifyDataSetChanged() // Aggiungi questa linea per aggiornare l'adapter

            }
        }
    }
    private fun getItems(
        id: Int,
        callback: (ArrayList<ItemViewModelCard>) -> Unit // Callback per restituire il risultato nullable
    ) {
        val query = "select * from carte where id_persona = '$id';"
        val data = ArrayList<ItemViewModelCard>()

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {

                        var completedCount = 0 // Contatore per tenere traccia del numero di chiamate completate

                        for (i in 0 until queryset.size()) {
                            val item = queryset.get(i).asJsonObject

                            data.add(ItemViewModelCard(id,item.get("titolare").asString,item.get("numero_carta").asString,item.get("data_scadenza").asString,item.get("cvv").asString))
                            completedCount++
                                // Verifica se tutte le chiamate sono state completate
                                if (completedCount == queryset.size()) {
                                    callback(data)
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
}