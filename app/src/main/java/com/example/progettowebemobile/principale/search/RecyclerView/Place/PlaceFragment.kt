package com.example.progettowebemobile.principale.search.RecyclerView.Place

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.FragmentPlaceBinding
import com.example.progettowebemobile.databinding.FragmentRecyclerviewSearchBinding
import com.example.progettowebemobile.entity.Luogo
import com.example.progettowebemobile.principale.account.ItemsViewModelPost
import com.google.gson.JsonObject
import com.google.gson.internal.bind.ArrayTypeAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class PlaceFragment : Fragment() {
    private lateinit var binding: FragmentPlaceBinding
    private lateinit var luogo: Luogo
    private lateinit var taskViewModel: TaskViewModel
    private var imagesList = mutableListOf<Bitmap>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val bundle = arguments
        luogo = bundle?.getSerializable("itemViewModel") as Luogo

        binding = FragmentPlaceBinding.inflate(inflater, container, false)
        var id = luogo.id_luogo
        getItems(id)
        binding.viewPager2.adapter = ViewPagerAdapter(imagesList)
        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        binding.searchFragmentBtnScegli.setOnClickListener {
            NewTaskSheet().show(requireActivity().supportFragmentManager, "newTaskTag")
        }



        binding.searchFragmentChiama.setOnClickListener{

        }
        binding.searchFragmentSito.setOnClickListener{

        }
        binding.searchFragmentInformazioni.text=luogo.descrizione
        binding.searchFragmentTvIndirizzo.text=luogo.indirizzo




        return binding.root
    }

    private fun getItems(id: Int) {
        val query = "select * from immagini where id_immagini = '$id';"

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {

                        val item = queryset.get(0).asJsonObject
                        var foto1 = item.get("foto1").asString
                        var foto2 = item.get("foto2").asString
                        var foto3 = item.get("foto3").asString

                        getImage(foto1)
                        getImage(foto2)
                        getImage(foto3)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }
            }
        )
    }
    private fun getImage(url: String) {
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
                            imagesList.add(avatar)
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