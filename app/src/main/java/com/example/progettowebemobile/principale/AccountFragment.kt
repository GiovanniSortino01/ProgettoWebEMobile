package com.example.progettowebemobile.principale

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.databinding.FragmentAccountBinding
import com.example.progettowebemobile.entity.Utente
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.R
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

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

        binding.userName.text = nomeUtente
        binding.userSurname.text = cognomeUtente
        binding.userRegistrationDate.text = dataInscrizioneUtente
        getImageProfilo(utente.immagine)


        return binding.root
    }


    private fun getImageProfilo(url: String){
        ClientNetwork.retrofit.getAvatar(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {
                        var avatar: Bitmap? = null
                        if (response.body()!=null) {
                            avatar = BitmapFactory.decodeStream(response.body()?.byteStream())
                            binding.imageView2.setImageBitmap(avatar)
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                }
            }
        )
    }
}
