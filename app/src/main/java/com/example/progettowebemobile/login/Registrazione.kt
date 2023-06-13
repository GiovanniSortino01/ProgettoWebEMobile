package com.example.progettowebemobile.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.ActivityLoginBinding
import com.example.progettowebemobile.databinding.ActivityRegistrazioneBinding

class Registrazione : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrazioneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrazioneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrationButtonBack.setOnClickListener{
            finish()
        }
        binding.registrationButtonSignin.setOnClickListener{

        }

    }
}