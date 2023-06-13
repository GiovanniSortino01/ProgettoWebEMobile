package com.example.progettowebemobile.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.db_connection.ClientNetwork
import com.example.db_connection.RequestLogin
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.ActivityLoginBinding
import com.example.progettowebemobile.principale.MainPrincipale
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var intentPrincipale :Intent
    private lateinit var intentRegistrazione :Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtnLogin.setOnClickListener{

        }

        binding.loginButnSignin.setOnClickListener{
            intentRegistrazione = Intent(this, Registrazione::class.java)
            startActivity(intentRegistrazione)
        }

    }

    private fun loginUtente (requestLogin: RequestLogin){

        val query = "select * from utenti where email = '${requestLogin.username}' and password = '${requestLogin.password}';"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    if (response.isSuccessful) { //Se non ci sono stati errori di connessione con il server
                        Log.i("onResponse", "Sono dentro il primo if. dim response: ${(response.body()?.get("queryset") as JsonArray).size()}")
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            Log.i("onResponse", "Sono dentro il secondo if. e chiamo la getImageProfilo")
                            startActivity(intentPrincipale)

                        } else {
                            Toast.makeText(applicationContext,"credenziali errate", Toast.LENGTH_LONG).show()
                            //binding.progressBar.visibility = View.GONE
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                    //Toast.makeText(this@MainActivity,"onFailure1", Toast.LENGTH_SHORT).show()
                    Toast.makeText(applicationContext, "Errore connessione al Database", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}