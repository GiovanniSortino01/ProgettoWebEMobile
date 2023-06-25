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
import com.example.progettowebemobile.principale.NewTaskSheet
import com.example.progettowebemobile.principale.TaskViewModel
import com.example.progettowebemobile.principale.ViewPagerAdapter
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
    private var imagesList = mutableListOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val bundle = arguments
        luogo = bundle?.getSerializable("itemViewModel") as Luogo

        binding = FragmentPlaceBinding.inflate(inflater, container, false)

        binding.viewPager2.adapter = ViewPagerAdapter(imagesList)
        binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
        binding.searchFragmentBtnScegli.setOnClickListener {
            NewTaskSheet().show(requireActivity().supportFragmentManager, "newTaskTag")
        }
        postToList()



        return binding.root
    }

    private fun addToList(image: Int){
        imagesList.add(image)
    }

    private fun postToList() {
        for (i in 1..5) {
            addToList(R.drawable.evento1)
            addToList(R.drawable.evento2)
            addToList(R.drawable.colosseo)
            addToList(R.drawable.picchu)

        }
    }

}