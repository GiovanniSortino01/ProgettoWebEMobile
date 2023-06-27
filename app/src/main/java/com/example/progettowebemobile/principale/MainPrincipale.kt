package com.example.progettowebemobile.principale

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.progettowebemobile.R
import com.example.progettowebemobile.entity.Utente
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.databinding.ActivityMainPrincipaleBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainPrincipale : AppCompatActivity() {
    private lateinit var binding: ActivityMainPrincipaleBinding
    private var buffer = Buffer()
    private lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPrincipaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Navigation Drawer
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.home_nav_open, R.string.home_nav_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> Toast.makeText(
                    applicationContext,
                    "Clicked Home",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.nav_prenotazioni -> Toast.makeText(
                    applicationContext,
                    "Clicked Prenotazioni",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.nav_preferiti -> Toast.makeText(
                    applicationContext,
                    "Clicked Preferti",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.nav_chiSiamo -> Toast.makeText(
                    applicationContext,
                    "Clicked Chi siamo",
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }

        //fine Navigation Drawer

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
    //funzione Navigation Drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true}

        return super.onOptionsItemSelected(item)
    }

}
