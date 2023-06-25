package com.example.progettowebemobile.principale

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.progettowebemobile.databinding.FragmentPagamentiBinding



class PagamentiFragment : Fragment() {

    private lateinit var binding : FragmentPagamentiBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPagamentiBinding.inflate(inflater, container, false)




        return binding.root
    }


}