package com.example.progettowebemobile.principale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.searchBtnRestaurant.setOnClickListener {
            val resultString = "ristorante"
            setFragmentResult("requestKey", bundleOf("bundleKey" to resultString))
            findNavController().navigate(R.id.action_searchFragment_to_reciclerViewSearch)
        }
        binding.searchBtnHotel.setOnClickListener {
            val resultString = "hotel"
            setFragmentResult("requestKey", bundleOf("bundleKey" to resultString))
            findNavController().navigate(R.id.action_searchFragment_to_reciclerViewSearch)
        }
        binding.searchBtnMonuments.setOnClickListener {
            val resultString = "monumento"
            setFragmentResult("requestKey", bundleOf("bundleKey" to resultString))
            findNavController().navigate(R.id.action_searchFragment_to_reciclerViewSearch)
        }
        binding.searchBtnPeople.setOnClickListener {
            val result
            String = "persona"
            setFragmentResult("requestKey", bundleOf("bundleKey" to resultString))
            findNavController().navigate(R.id.action_searchFragment_to_reciclerViewSearch)
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Esegui le azioni desiderate qui
                // ad esempio, torna indietro o chiudi il Fragment
                findNavController().navigate(R.id.action_searchFragment_to_homeFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        return binding.root
    }


}