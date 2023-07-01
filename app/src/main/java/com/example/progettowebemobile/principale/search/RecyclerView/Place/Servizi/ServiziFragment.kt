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

        var textView = servizi.fitness.replace("\\n", "\n").replace("\\u2022", "•")
        binding.serviziFitnessSpecifiche.text=textView

         textView = servizi.generali.replace("\\n", "\n").replace("\\u2022", "•")
        binding.serviziGeneraliSpecifiche.text= textView

         textView = servizi.wifi.replace("\\n", "\n").replace("\\u2022", "•")
        binding.serviziInternetSpecifiche.text=textView

        textView =  servizi.fitness.replace("\\n", "\n").replace("\\u2022", "•")
        binding.serviziFitnessSpecifiche.text=textView

         textView = servizi.serviziopulizia.replace("\\n", "\n").replace("\\u2022", "•")
        binding.serviziPuliziaSpecifiche.text=textView

        textView = servizi.servizioreception.replace("\\n", "\n").replace("\\u2022", "•")
        binding.serviziReceptionSpecifiche.text= textView

         textView = servizi.servizioincamera.replace("\\n", "\n").replace("\\u2022", "•")
        binding.serviziInCameraSpecifiche.text= textView

         textView = servizi.ciboebevande.replace("\\n", "\n").replace("\\u2022", "•")
        binding.serviziCiboBevandeSpecifiche.text= textView

         textView = servizi.trasporti.replace("\\n", "\n").replace("\\u2022", "•")
        binding.serviziTrasportiSpecifiche.text= textView

         textView = servizi.tipidicamere.replace("\\n", "\n").replace("\\u2022", "•")
        binding.serviziTipiCamereSpecifiche.text=textView










        return binding.root
    }

}
