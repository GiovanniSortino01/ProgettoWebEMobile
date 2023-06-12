package com.example.progettowebemobile.principale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.ActivityMainPrincipaleBinding
import com.example.progettowebemobile.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainPrincipale : AppCompatActivity() {
    //private lateinit var binding: ActivityMainPrincipaleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_principale)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNavBar)
        val navController = findNavController(R.id.fragmentPrincipale)
        bottomNavigationView.setupWithNavController(navController)




    }
}