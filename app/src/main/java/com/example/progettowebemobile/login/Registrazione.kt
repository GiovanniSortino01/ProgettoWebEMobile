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
import java.util.Date

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
            var nome = binding.registrationEtName.text.toString()
            var cognome = binding.registrationEtSurname .text.toString()
            var email = binding.registrationEtEmail.text.toString()
            var password1 = binding.registrationEtPassword.text.toString()
            var password2 = binding.registrationEtConfermaPassword.text.toString()
            if(nome.isEmpty()||cognome.isEmpty()||email.isEmpty()||password1.isEmpty()||password2.isEmpty()){
                utils.PopError(getString(R.string.register_empty_title),getString(R.string.register_empty_text),this)
            }else if(password1 != password2){
                utils.PopError(getString(R.string.register_password_not_conform_title),getString(R.string.register_password_not_conform_text),this)
            }else {
                verificaEsistenzaEmail(email) { emailExists ->
                    if (emailExists == true) {
                        utils.PopError(getString(R.string.register_email_exist_title),getString(R.string.register_email_exist_text),this)
                    } else if (emailExists == false) {
                        insert(nome,cognome,email,password1)
                    } else {
                        // Il valore è null (errore di connessione o fallimento)
                    }
                }

            }
        }
    }

    private fun verificaEsistenzaEmail(
        email: String,
        callback: (Boolean?) -> Unit // Callback per restituire il risultato nullable
    ) {
        val query = "select * from utenti where email = '$email';"

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        val resultSetSize = (response.body()?.get("queryset") as JsonArray).size()
                        val emailExists = resultSetSize >= 1
                        callback(emailExists) // Chiamata alla callback con il risultato booleano nullable
                    } else {
                        callback(null) // Chiamata alla callback con valore null in caso di errore di connessione
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    callback(null) // Chiamata alla callback con valore null in caso di fallimento
                }
            }
        )
    }
    fun insert(nome: String,cognome:String, email: String, password: String){
        var data = Date()
        val query = "insert into utenti (nome, cognome, data_inscrizione, email, password) values ('$nome', '$cognome', '$data', '$email', '$password');"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {


                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server

                    Log.i("LOG", "Query creata:$query ")
                    if (response.isSuccessful) { //Se non ci sono stati errori di connessione con il server
                        utils.PopError(getString(R.string.register_new_account_title),getString(R.string.register_new_account_title),this@Registrazione)

                    }else{

                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.

                    utils.PopError(getString(R.string.login_db_error_title), getString(R.string.login_db_error),this@Registrazione)
                }
            }
        )
    }
}



/*
val query = "insert todo utenti (nome,cognome,email,password) value ('${nome}','${cognome}','${email}','${password}');"

ClientNetwork.retrofit.login(query).enqueue(
object : Callback<JsonObject> {

    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server
        if (response.isSuccessful) { //Se non ci sono stati errori di connessione con il server
            utils.PopError(getString(R.string.register_new_account_title),getString(R.string.register_new_account_title))
        }
    }
    override fun onFailure(call: Call<JsonObject>, t: Throwable) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
        utils.PopError(getString(R.string.login_db_error_title), getString(R.string.login_db_error),this@Registrazione)
    }
}
)*/