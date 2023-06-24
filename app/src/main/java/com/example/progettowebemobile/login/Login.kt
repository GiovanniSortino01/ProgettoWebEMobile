package com.example.progettowebemobile.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.db_connection.ClientNetwork
import com.example.db_connection.RequestLogin
import com.example.progettowebemobile.R
import com.example.progettowebemobile.Utils
import com.example.progettowebemobile.databinding.ActivityLoginBinding
import com.example.progettowebemobile.entity.Utente
import com.example.progettowebemobile.principale.MainPrincipale
import com.example.progettowebemobile.Buffer
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var intentPrincipale :Intent
    private lateinit var intentRegistrazione :Intent
    private lateinit var requestLogin: RequestLogin
    private var utils: Utils=Utils()
    private lateinit var utente: Utente

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtnLogin.setOnClickListener {
            var email = binding.loginEtEmail.text.toString()
            var password = binding.loginEtPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                utils.PopError(
                    getString(R.string.login_error_title),
                    getString(R.string.login_error_text),
                    this
                )
            } else {
                intentPrincipale = Intent(this, MainPrincipale::class.java)
                intentPrincipale.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                requestLogin = RequestLogin(email, password)
                loginUtente(requestLogin)
            }
        }

        binding.loginTVSingin.setOnClickListener {
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
                    if (response.isSuccessful) {
                        val queryset = response.body()?.getAsJsonArray("queryset")
                        if (queryset?.size() == 1) {
                            val utenteJsonObject = queryset.get(0).asJsonObject
                            val id = utenteJsonObject.get("id").asInt
                            val nome = utenteJsonObject.get("nome").asString
                            val cognome = utenteJsonObject.get("cognome").asString
                            val data = utenteJsonObject.get("datainscrizione").asString
                            val email = utenteJsonObject.get("email").asString
                            val password = utenteJsonObject.get("password").asString
                            val immagine = utenteJsonObject.get("immagine").asString
                            utente = Utente(id,nome,cognome,data,email,password,immagine)
                            val buffer = Buffer()
                            buffer.setUtente(utente)
                            intentPrincipale.putExtra("Utente", utente);
                            startActivity(intentPrincipale)

                        } else {
                            utils.PopError(getString(R.string.login_credenziali_errate_titolo), getString(R.string.login_credenziali_errate),this@Login)
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                    //Toast.makeText(this@MainActivity,"onFailure1", Toast.LENGTH_SHORT).show()
                    Log.i("onFailure", "Sono dentro al onFailure")
                    utils.PopError(getString(R.string.login_db_error_title), getString(R.string.login_db_error),this@Login)
                }
            }
        )
    }
}


