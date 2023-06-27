package com.example.progettowebemobile.principale.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.FragmentPlaceBinding
import com.example.progettowebemobile.databinding.FragmentPrenotazioniBinding

class PrenotazioniFragment : Fragment() {

    private lateinit var binding: FragmentPrenotazioniBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPrenotazioniBinding.inflate(inflater, container, false)




        return binding.root
    }


}