package com.example.progettowebemobile.principale.home.pagamenti

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.R
import com.example.progettowebemobile.Utils
import com.example.progettowebemobile.databinding.FragmentPagamentiBinding
import com.example.progettowebemobile.principale.account.ItemsViewModelPost
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date

class PagamentiFragment : Fragment() {

    private lateinit var binding : FragmentPagamentiBinding
    private var buffer= Buffer()
    private var utente = buffer.getUtente()
    private var utils = Utils()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPagamentiBinding.inflate(inflater, container, false)
        binding.pagamentiRv.layoutManager = LinearLayoutManager(requireContext())
        loadRecyclerViewData()

        binding.floatingActionButton2.setOnClickListener{
            popAdd()
        }

        return binding.root
    }
    private fun loadRecyclerViewData() {
        utente?.let {
            getItems(it.id) { data ->
                val adapter = CardAdapter(data,requireContext(), utente!!)
                binding.pagamentiRv.adapter = adapter

                adapter.setOnClickListener(object : CardAdapter.OnClickListener {
                    override fun onClick(position: Int, model: ItemViewModelCard) {
                        Log.i(ContentValues.TAG, "Index ${position + 1}")
                    }

                })
                adapter.notifyDataSetChanged() // Aggiungi questa linea per aggiornare l'adapter

            }
        }
    }
    private fun getItems(
        id: Int,
        callback: (ArrayList<ItemViewModelCard>) -> Unit // Callback per restituire il risultato nullable
    ) {
        val query = "select * from carte where id_persona = '$id';"
        val data = ArrayList<ItemViewModelCard>()
        var nessuna_carta_image = binding.imageView
        var nessuna_carta_text = binding.textView5

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {
                        nessuna_carta_image.visibility=View.GONE
                        nessuna_carta_text.visibility=View.GONE
                        var completedCount = 0 // Contatore per tenere traccia del numero di chiamate completate

                        for (i in 0 until queryset.size()) {
                            val item = queryset.get(i).asJsonObject

                            data.add(ItemViewModelCard(id,item.get("titolare").asString,item.get("numero_carta").asString,item.get("data_scadenza").asString,item.get("cvv").asString))
                            completedCount++
                                // Verifica se tutte le chiamate sono state completate
                                if (completedCount == queryset.size()) {
                                    callback(data)
                                }
                            }
                    } else {
                        nessuna_carta_image.visibility=View.VISIBLE
                        nessuna_carta_text.visibility=View.VISIBLE
                        callback(data)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Chiamata alla callback con valore null in caso di fallimento
                    callback(data)
                }
            }
        )
    }

    private fun popAdd() {

        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.pop_up_add_card, null)
        val popupButtonClose = popupView.findViewById<Button>(R.id.close_button)
        val popupButtonAdd = popupView.findViewById<Button>(R.id.add_botton)
        val ET_titolare = popupView.findViewById<EditText>(R.id.Et_titolare)
        val ET_number = popupView.findViewById<EditText>(R.id.Et_number_card)
        val ET_date = popupView.findViewById<EditText>(R.id.Et_data_card)
        val ET_cvv = popupView.findViewById<EditText>(R.id.Et_cvv_card)

        val alertDialogBuilder = AlertDialog.Builder(context).setView(popupView)
        val alertDialog = alertDialogBuilder.create()

        popupButtonClose.setOnClickListener {
            alertDialog.dismiss()
        }

        popupButtonAdd.setOnClickListener {
            var id = utente?.id
            if(id != null) {
                val numero = ET_number.text.toString()
                val titolare = ET_titolare.text.toString()
                val cvv = ET_cvv.text.toString()
                val data = ET_date.text.toString()
                val firstTwoChars = data.substring(0, 2)
                val numeroSenzaSpazi = numero.replace(" ", "")

                if (numeroSenzaSpazi.isEmpty() || cvv.isEmpty() || data.isEmpty() || titolare.isEmpty()) {
                    utils.PopError(
                        getString(R.string.card_newCard_error_title),
                        getString(R.string.card_newCard_error_text),
                        requireContext()
                    )
                } else {
                    exist(numeroSenzaSpazi) { exists ->
                        if (exists) {
                            //già esistente
                            utils.PopError(
                                getString(R.string.card_newCard_error_general),
                                getString(R.string.card_newCard_error_numberExist),
                                requireContext()
                            )
                        } else if (numeroSenzaSpazi.length != 16 || !TextUtils.isDigitsOnly(
                                numeroSenzaSpazi
                            )
                        ) {
                            // Numero non valido
                            utils.PopError(
                                getString(R.string.card_newCard_error_general),
                                getString(R.string.card_newCard_error_numberError),
                                requireContext()
                            )
                        } else if (cvv.length != 3) {
                            // CVV non valido
                            utils.PopError(
                                getString(R.string.card_newCard_error_general),
                                getString(R.string.card_newCard_error_cvvError),
                                requireContext()
                            )
                        }else if(!verificaFormatoStringa(data)){
                            utils.PopError(
                                getString(R.string.card_newCard_error_general),
                                getString(R.string.card_newCard_error_dataMeseError),
                                requireContext()
                            )
                        }else if(firstTwoChars.toInt()>12){
                            //Data non valida
                            utils.PopError(
                                getString(R.string.card_newCard_error_general),
                                getString(R.string.card_newCard_error_dataMeseError),
                                requireContext()
                            )
                        } else if (!verificaData(data)) {
                            // Carta scaduta
                            utils.PopError(
                                getString(R.string.card_newCard_error_general),
                                getString(R.string.card_newCard_error_dataError),
                                requireContext()
                            )
                        } else {
                            // Inserisci i dati nel database
                            insert(id, numeroSenzaSpazi, data, cvv, titolare)
                            alertDialog.dismiss()
                        }
                    }
                }
            }
    }
        val builder = AlertDialog.Builder(context)
        builder.setView(popupView)

        //visualizza il pop-up
        alertDialog.show()
    }

    private fun insert(id:Int, numero: String, dataScadenza: String, cvv: String, titolare: String) {
        val query =
            "INSERT INTO carte (id_persona, numero_carta, cvv, data_scadenza, titolare) VALUES ('$id', '$numero', '$cvv', '$dataScadenza', '$titolare');"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {


                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server

                    Log.i("LOG", "Query creata:$query ")
                    if (response.isSuccessful) { //Se non ci sono stati errori di connessione con il server
                        // utils.PopError(getString(R.string.register_new_account_title),getString(R.string.register_new_account_title),this@Registrazione)
                        utente?.card?.add(numero)
                       loadRecyclerViewData()
                    } else {
                        Log.i("LOG", "Errore durante la registrazione ")
                    }
                }

                override fun onFailure(
                    call: Call<JsonObject>,
                    t: Throwable
                ) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.

                    //utils.PopError(getString(R.string.login_db_error_title), getString(R.string.login_db_error),this@Registrazione)
                }
            }
        )
    }

    private fun verificaData(meseAnno: String): Boolean {
        val formatoData = SimpleDateFormat("MM/yy")
        val currentDate = Calendar.getInstance().time

        try {
            val data1 = formatoData.parse(meseAnno)
            return data1.after(currentDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }




    private fun exist(id: String, callback: (Boolean) -> Unit) {
        val query = "select * from carte where numero_carta = '$id';"

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {
                        callback(true)
                    } else {
                        callback(false)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    // Chiamata alla callback con valore false in caso di fallimento
                    callback(false)
                }
            }
        )
    }

    fun verificaFormatoStringa(data: String): Boolean {
        val regexPattern = """^\d{2}/\d{2}$""".toRegex()
        return regexPattern.matches(data)
    }


}