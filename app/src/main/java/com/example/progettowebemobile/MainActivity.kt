package com.example.progettowebemobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.navigation.Navigation
import com.example.progettowebemobile.login.Login
import com.example.progettowebemobile.principale.MainPrincipale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val intentLogin = Intent(this, Login::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({

            startActivity(intentLogin)
            finish()
        }, 2000)

    }



}