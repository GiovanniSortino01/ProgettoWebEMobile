package com.example.progettowebemobile.principale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.progettowebemobile.R
import com.example.progettowebemobile.entity.Utente
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.databinding.ActivityMainPrincipaleBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainPrincipale : AppCompatActivity() {
    private lateinit var binding: ActivityMainPrincipaleBinding
    private var buffer = Buffer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPrincipaleBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Bottom Navigation View

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNavBar)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            val navController = findNavController(R.id.fragmentPrincipale)

            when (menuItem.itemId) {

                R.id.homeFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.gpsFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.gpsFragment)
                    true
                }
                R.id.searchFragment -> {
                    navController.popBackStack()
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.accountFragment -> {
                    navController.popBackStack()

                    navController.navigate(R.id.accountFragment)
                    true
                }
                else -> false
            }
        }/*
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.btmNavBar)
        val navController = findNavController(R.id.fragmentPrincipale)
        bottomNavigationView.setupWithNavController(navController)
*/
        val intent = intent
        val utente = intent.getSerializableExtra("Utente") as Utente
        buffer.setUtente(utente)


    }
}
