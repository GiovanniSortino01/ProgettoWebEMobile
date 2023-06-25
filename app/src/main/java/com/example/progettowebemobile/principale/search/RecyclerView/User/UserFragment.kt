package com.example.progettowebemobile.principale.search.RecyclerView.User

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.FragmentPlaceBinding
import com.example.progettowebemobile.databinding.FragmentUserBinding
import com.example.progettowebemobile.entity.Luogo
import com.example.progettowebemobile.entity.Persona

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
        binding.text.text = persona.nome


        return binding.root
    }

}