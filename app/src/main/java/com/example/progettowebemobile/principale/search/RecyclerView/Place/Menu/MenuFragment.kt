package com.example.progettowebemobile.principale.search.RecyclerView.Place.Menu

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
import com.example.progettowebemobile.databinding.FragmentMenuBinding
import com.example.progettowebemobile.entity.Luogo
import com.example.progettowebemobile.principale.search.RecyclerView.Place.ItemsViewModelRecenzioni
import com.example.progettowebemobile.principale.search.RecyclerView.Place.RecenzioniAdapter
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var luogo: Luogo


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val bundle = arguments
        luogo = bundle?.getSerializable("itemViewModel") as Luogo
        binding = FragmentMenuBinding.inflate(inflater,container,false)
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuBtnAntipasti.setOnClickListener{
            loadRecyclerViewData("antipasto")
        }
        binding.menuBtnPrimi.setOnClickListener{
            loadRecyclerViewData("primo")
        }
        binding.menuBtnSecondi.setOnClickListener{
            loadRecyclerViewData("secondo")
        }
        binding.menuBtnDolci.setOnClickListener{
            loadRecyclerViewData("dolce")
        }
        binding.menuBtnBevande.setOnClickListener{
            loadRecyclerViewData("bevenda")
        }



        return binding.root
    }
    private fun loadRecyclerViewData(tipo:String) {
        var id=luogo.id_luogo
        getItems(id,tipo) { data ->
            val adapter = MenuAdapter(data,requireContext())
            binding.menuRecyclerView.adapter = adapter
            adapter.setOnClickListener(object : MenuAdapter.OnClickListener {
                override fun onClick(position: Int, model: ItemsViewModelMenu) {
                    Log.i(ContentValues.TAG,"Index ${position + 1}")
                }
            })
            adapter.notifyDataSetChanged() // Aggiungi questa linea per aggiornare l'adapter
        }
    }

    private fun getItems(id: Int,tipo:String, callback: (ArrayList<ItemsViewModelMenu>) -> Unit) {
        val query = "select * from cibi where id_luogo = '$id' and tipo='$tipo';"
        val data = ArrayList<ItemsViewModelMenu>()

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {

                        var completedCount = 0 // Contatore per tenere traccia del numero di chiamate completate

                        for (i in 0 until queryset.size()) {
                            val item = queryset.get(i).asJsonObject
                            var id_cibo = item.get("id_cibo").asInt
                            var id_luogo = id
                            var tipo = item.get("tipo").asString
                            var nome = item.get("nome").asString
                            var ingredienti = item.get("ingredienti").asString
                            var prezzo = item.get("prezzo").asString

                                data.add(
                                    ItemsViewModelMenu(
                                        id_cibo,
                                        id_luogo,
                                        tipo,
                                        nome,
                                        ingredienti,
                                        prezzo,
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
