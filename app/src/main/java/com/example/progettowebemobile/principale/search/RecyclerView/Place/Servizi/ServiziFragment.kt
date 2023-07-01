package com.example.progettowebemobile.principale.search.RecyclerView.Place.Servizi

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.databinding.FragmentServiziBinding
import com.example.progettowebemobile.entity.Luogo
import com.example.progettowebemobile.entity.Servizi
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiziFragment : Fragment() {

    private lateinit var binding: FragmentServiziBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentServiziBinding.inflate(inflater,container,false)
        val bundle = arguments
        var servizi = bundle?.getSerializable("itemViewModel") as Servizi
        binding.serviziFitnessSpecifiche.text= servizi.fitness
        binding.serviziGeneraliSpecifiche.text= servizi.generali
        binding.serviziInternetSpecifiche.text= servizi.wifi
        binding.serviziPuliziaSpecifiche.text= servizi.serviziopulizia
        binding.serviziReceptionSpecifiche.text= servizi.servizioreception
        binding.serviziInCameraSpecifiche.text= servizi.servizioincamera
        binding.serviziCiboBevandeSpecifiche.text= servizi.ciboebevande
        binding.serviziTrasportiSpecifiche.text= servizi.trasporti
        binding.serviziTipiCamereSpecifiche.text= servizi.tipidicamere

        return binding.root
    }

}
