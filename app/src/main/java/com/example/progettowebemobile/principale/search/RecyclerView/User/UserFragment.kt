package com.example.progettowebemobile.principale.search.RecyclerView.User

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.FragmentAccountBinding
import com.example.progettowebemobile.databinding.FragmentPlaceBinding
import com.example.progettowebemobile.databinding.FragmentUserBinding
import com.example.progettowebemobile.entity.Luogo
import com.example.progettowebemobile.entity.Persona
import com.example.progettowebemobile.principale.account.ItemsViewModelPost
import com.example.progettowebemobile.principale.account.PersonalAccountAdapter
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private lateinit var persona: Persona

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        persona = bundle?.getSerializable("itemViewModel") as Persona


        binding = FragmentUserBinding.inflate(inflater, container, false)
        binding.userName.text = persona.nome
        binding.userSurname.text = persona.cognome
        binding.userRegistrationDate.text=persona.data
        getImageProfilo(persona.immagine)
        binding.userRecycleView.layoutManager = LinearLayoutManager(requireContext())
        loadRecyclerViewData()
        return binding.root
    }
    private fun loadRecyclerViewData() {
        getItems(persona.id) { data ->
            val adapter = PersonalAccountAdapter(data,requireContext(),null)
            binding.userRecycleView.adapter = adapter

            adapter.setOnClickListener(object : PersonalAccountAdapter.OnClickListener {
                override fun onClick(position: Int, model: ItemsViewModelPost) {
                    Log.i(ContentValues.TAG, "Index ${position + 1}")
                }
            })
            adapter.notifyDataSetChanged() // Aggiungi questa linea per aggiornare l'adapter
        }
    }
    private fun getItems(id: Int, callback: (ArrayList<ItemsViewModelPost>) -> Unit) {
        val query = "select * from post where id_persona = '$id' ORDER BY data ASC;"
        val data = ArrayList<ItemsViewModelPost>()

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {

                        var completedCount =
                            0 // Contatore per tenere traccia del numero di chiamate completate

                        for (i in 0 until queryset.size()) {
                            val item = queryset.get(i).asJsonObject
                            var foto = item.get("foto").asString

                            getImageProfiloRecyclerView(foto) { avatar ->
                                completedCount++
                                if (avatar != null) {
                                    data.add(
                                        ItemsViewModelPost(
                                            item.get("id_post").asInt,
                                            persona.nome,
                                            persona.cognome,
                                            avatar,
                                            item.get("descrizione").asString,
                                            item.get("luogo").asString,
                                            item.get("data").asString
                                        )
                                    )
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
    private fun getImageProfilo(url: String) {
        ClientNetwork.retrofit.getAvatar(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        var avatar: Bitmap? = null
                        if (response.body() != null) {
                            avatar = BitmapFactory.decodeStream(response.body()?.byteStream())
                            binding.imageView2.setImageBitmap(avatar)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ResponseBody>,
                    t: Throwable
                ) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                }
            }
        )
    }

}