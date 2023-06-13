package com.example.progettowebemobile.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.db_connection.ClientNetwork
import com.example.db_connection.RequestLogin
import com.example.progettowebemobile.R
import com.example.progettowebemobile.Utils
import com.example.progettowebemobile.databinding.ActivityLoginBinding
import com.example.progettowebemobile.databinding.ActivityRegistrazioneBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Registrazione : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrazioneBinding
    private var utils = Utils()
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

    private fun singUpUtente (email: String){

        val query = "select * from utenti where email = '${email}';"

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server
                    if (response.isSuccessful) { //Se non ci sono stati errori di connessione con il server
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            utils.PopError(getString(R.string.register_exist_title),getString(R.string.register_exist_text),this@Registrazione)
                        } else {

                            utils.PopError(getString(R.string.register_new_account_title),getString(R.string.register_new_account_text),this@Registrazione)

                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                    utils.PopError(getString(R.string.login_db_error_title), getString(R.string.login_db_error),this@Registrazione)
                }
            }
        )
    }
}