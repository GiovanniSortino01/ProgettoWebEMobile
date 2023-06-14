package com.example.progettowebemobile.principale

import android.content.Intent
import android.os.Bundle
import android.provider.Contacts.Intents
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.FragmentAccountBinding
import com.example.progettowebemobile.entity.Utente
import com.example.progettowebemobile.principale.search.Buffer

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var utente: Utente
    private var buffer = Buffer()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val intent = Intent()
       utente = buffer.getUtente()!!

        binding = FragmentAccountBinding.inflate(inflater, container, false)
        val nomeUtente = utente.nome
        val cognomeUtente = utente.cognome
        val dataInscrizioneUtente = utente?.data
        binding.userName.text=nomeUtente
        binding.userSurname.text = cognomeUtente
        binding.userRegistrationDate.text = dataInscrizioneUtente
        return binding.root
    }
}
