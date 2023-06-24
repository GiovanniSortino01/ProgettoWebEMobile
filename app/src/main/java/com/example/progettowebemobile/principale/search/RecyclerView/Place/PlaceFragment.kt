package com.example.progettowebemobile.principale.search.RecyclerView.Place

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val bundle = arguments
        luogo = bundle?.getSerializable("itemViewModel") as Luogo

        binding = FragmentPlaceBinding.inflate(inflater, container, false)
        binding.tx.text = luogo.nome


        return binding.root
    }

}