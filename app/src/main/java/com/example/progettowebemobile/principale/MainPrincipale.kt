package com.example.progettowebemobile.principale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.progettowebemobile.R
import com.example.progettowebemobile.entity.Utente
import com.example.progettowebemobile.Buffer
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainPrincipale : AppCompatActivity() {
    //private lateinit var binding: ActivityMainPrincipaleBinding
    private var buffer = Buffer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_principale)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNavBar)
        val navController = findNavController(R.id.fragmentPrincipale)
        bottomNavigationView.setupWithNavController(navController)

        val intent = intent
        val utente = intent.getSerializableExtra("Utente") as Utente
        buffer.setUtente(utente)


    }
}