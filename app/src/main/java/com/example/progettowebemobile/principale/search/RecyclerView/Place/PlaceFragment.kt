package com.example.progettowebemobile.principale.search.RecyclerView.Place

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.Buffer.Companion.utente
import com.example.progettowebemobile.R
import com.example.progettowebemobile.Utils
import com.example.progettowebemobile.databinding.FragmentPlaceBinding
import com.example.progettowebemobile.entity.Luogo
import com.example.progettowebemobile.entity.Utente
import com.example.progettowebemobile.principale.account.ItemsViewModelPost
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class PlaceFragment : Fragment() {
    private lateinit var binding: FragmentPlaceBinding
    private lateinit var luogo: Luogo
    private lateinit var taskViewModel: TaskViewModel
    private var utils = Utils()
    private var imagesList = mutableListOf<Bitmap>()
    private var backButtonEnabled=false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceBinding.inflate(inflater, container, false)
        val bundle = arguments
        luogo = bundle?.getSerializable("itemViewModel") as Luogo
        utente = bundle?.getSerializable("utente") as Utente

        viewpage2()
        setting()
        loadRecyclerViewData()

        binding.searchFragmentRvRecensioni.layoutManager = LinearLayoutManager(requireContext())

        binding.searchFragmentChiama.setOnClickListener{
            if (checkTelephonePermission()) {
                openTelefono(requireContext(),luogo.numero_cellulare)
            } else {
                // Richiedi i permessi
                requestTelephonePermission()
            }
        }

        binding.searchFragmentSito.setOnClickListener{
            val url = luogo.sitoweb
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        binding.searchFragmentServizi.setOnClickListener{
            if(luogo.tipo.equals("ristorante")){
                findNavController().navigate(R.id.action_placeFragment_to_menuFragment)

            }else if(luogo.tipo.equals("hotel")) {
                findNavController().navigate(R.id.action_placeFragment_to_serviziFragment)
            }
        }
        binding.searchFragmentBtnRecensioni.setOnClickListener{
            popAdd(utente!!)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Handler().postDelayed({
                    backButtonEnabled = true
                }, 1000)
                if (backButtonEnabled) {
                    // Esegui l'azione di "Torna indietro" se il pulsante è abilitato
                    findNavController().navigateUp()
                    backButtonEnabled = false
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        return binding.root
    }
    private fun viewpage2(){
        var id = luogo.id_luogo
        getItemsImage(id) { data ->
            var adapter = ViewPagerAdapter(data)
            binding.viewPager2.adapter = adapter
            binding.viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]
            adapter.notifyDataSetChanged()
        }
        binding.searchFragmentBtnScegli.setOnClickListener {
            NewTaskSheet().show(requireActivity().supportFragmentManager, "newTaskTag")
        }
    }
    private fun getItemsImage(id: Int,callback: (ArrayList<Bitmap>) -> Unit) {
        val query = "select * from immagini where id_immagini = '$id';"

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {

                        val item = queryset.get(0).asJsonObject
                        var foto1 = item.get("foto1").asString
                        var foto2 = item.get("foto2").asString
                        var foto3 = item.get("foto3").asString
                        val url = listOf(foto1, foto2, foto3)


                        getImage(url) { images ->
                            callback(images)
                            // Puoi eseguire ulteriori operazioni con la lista completa di immagini qui
                        }

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }
            }
        )
    }
    private fun getImage(urlList: List<String>, callback: (ArrayList<Bitmap>) -> Unit) {
        var count = 0
        val data = ArrayList<Bitmap>()
        for (url in urlList) {
            ClientNetwork.retrofit.getAvatar(url).enqueue(
                object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            var avatar: Bitmap? = null
                            if (response.body() != null) {
                                avatar = BitmapFactory.decodeStream(response.body()?.byteStream())
                                data.add(avatar)
                            }
                        }

                        count++
                        if (count == urlList.size) {
                            callback(data)
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                    }
                }
            )
        }
    }



    private fun checkTelephonePermission(): Boolean {
        val permission = Manifest.permission.CALL_PHONE
        val result = ContextCompat.checkSelfPermission(requireContext(), permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestTelephonePermission() {
        val permission = Manifest.permission.CALL_PHONE

        if (shouldShowRequestPermissionRationale(permission)) {
            // Spiega all'utente perché sono necessari i permessi
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage(getString(R.string.dialog_text))
                .setTitle(getString(R.string.dialog_title))
                .setPositiveButton(R.string.dialog_concedi) { dialog, _ ->
                    dialog.dismiss()
                    requestPermissionLauncher.launch(permission)
                }
                .setNegativeButton(R.string.dialog_annulla) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            // Richiedi direttamente i permessi
            requestPermissionLauncher.launch(permission)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openTelefono(requireContext(),luogo.numero_cellulare)
            }
        }

    private fun openTelefono(context: Context, phoneNumber: Long) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    private fun setting(){
        binding.searchFragmentNomePosto.text=luogo.nome
        binding.searchFragmentInformazioni.text=luogo.descrizione
        binding.searchFragmentTvIndirizzo.text=luogo.indirizzo
        var tipo = luogo.tipo
        var numero = luogo.numero_cellulare
        val valueFromDatabase = luogo.come_arrivarci

        Log.i(TAG,"$valueFromDatabase")
        var textView = valueFromDatabase.replace("\\n", "\n").replace("\\u2022", "•")
        binding.searchFragmentComeModo.text=textView
        if(tipo.equals("monumento")&&(numero.compareTo(0)==0)){
            monumento()
        }
        else if(tipo.equals("monumento")){//evento
            evento()
        }
        else if(tipo.equals("hotel")){
            hotel()
        }
        else if(tipo.equals("ristorante")){
            ristorante()
        }
    }
    private fun monumento(){
        binding.searchFragmentTvPrenotazione.text=getString(R.string.Place_TvPrenotazioni_Monumento)
        binding.searchFragmentBtnScegli.text=getString(R.string.Place_btnCompra_Monumento)
        binding.searchFragmentBtnPrenota.text=getString(R.string.Place_btnPrenota_Monumento)
        binding.searchFragmentInformazioniGenerale.text=getString(R.string.Place_TvInformazioni_Monumento)

        binding.searchFragmentChiama.visibility = View.GONE
        binding.searchFragmentServizi.visibility = View.GONE
    }
    private fun evento(){
        binding.searchFragmentTvPrenotazione.text=getString(R.string.Place_TvPrenotazioni_Evento)
        binding.searchFragmentBtnScegli.text=getString(R.string.Place_btnCompra_Evento)
        binding.searchFragmentBtnPrenota.text=getString(R.string.Place_btnPrenota_Evento)
        binding.searchFragmentInformazioniGenerale.text=getString(R.string.Place_TvInformazioni_Evento)

        binding.searchFragmentChiama.visibility = View.VISIBLE
        binding.searchFragmentServizi.visibility = View.GONE
    }
    private fun hotel(){
        binding.searchFragmentTvPrenotazione.text=getString(R.string.Place_TvPrenotazioni_Hotel)
        binding.searchFragmentBtnScegli.text=getString(R.string.Place_btnCompra_Hotel)
        binding.searchFragmentBtnPrenota.text=getString(R.string.Place_btnPrenota_Hotel)
        binding.searchFragmentInformazioniGenerale.text=getString(R.string.Place_TvInformazioni_Hotel)

        binding.searchFragmentChiama.visibility = View.VISIBLE
        binding.searchFragmentServizi.visibility = View.VISIBLE
        binding.searchFragmentServizi.text = "Servizi"
    }
    private fun ristorante(){
        binding.searchFragmentTvPrenotazione.text=getString(R.string.Place_TvPrenotazioni_Ristorante)
        binding.searchFragmentBtnScegli.text=getString(R.string.Place_btnCompra_Ristorante)
        binding.searchFragmentBtnPrenota.text=getString(R.string.Place_btnPrenota_Ristorante)
        binding.searchFragmentInformazioniGenerale.text=getString(R.string.Place_TvInformazioni_Ristorante)

        binding.searchFragmentChiama.visibility = View.VISIBLE
        binding.searchFragmentServizi.visibility = View.VISIBLE
        binding.searchFragmentServizi.text = "Menù"
    }


    private fun loadRecyclerViewData() {
        var id=luogo.id_luogo
        getItems(id) { data ->
            val adapter = RecenzioniAdapter(data,requireContext())
            binding.searchFragmentRvRecensioni.adapter = adapter
            adapter.setOnClickListener(object : RecenzioniAdapter.OnClickListener {
                override fun onClick(position: Int, model: ItemsViewModelPost) {
                    Log.i(TAG,"Index ${position + 1}")
                }
            })
            adapter.notifyDataSetChanged() // Aggiungi questa linea per aggiornare l'adapter
        }
    }

    private fun getItems(id: Int, callback: (ArrayList<ItemsViewModelRecenzioni>) -> Unit) {
        val query = "select * from recenzioni where id_luogo = '$id';"
        val data = ArrayList<ItemsViewModelRecenzioni>()

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {

                        var completedCount = 0 // Contatore per tenere traccia del numero di chiamate completate

                        for (i in 0 until queryset.size()) {
                            val item = queryset.get(i).asJsonObject
                            var id_utente = item.get("id_persona").asInt
                            var id_recenzione = item.get("id_recenzione").asInt
                            var recenzioni = item.get("valutazione").asFloat
                            var descrizione = item.get("descrizione").asString
                            var nome = item.get("nome").asString
                            var data_publicazione = item.get("data_publicazione").asString
                            var id_luogo = luogo.id_luogo
                            var immagine = utente?.immagine
                            if(immagine!=null){
                            data.add(
                                ItemsViewModelRecenzioni(
                                    id_recenzione,
                                    id_utente,
                                    id_luogo,
                                    nome,
                                    descrizione,
                                    recenzioni,
                                    data_publicazione,
                                    immagine
                                )
                            )
                            }

                            // Incrementa completedCount dopo aver aggiunto un elemento a data
                            completedCount++

                            // Verifica se tutte le chiamate sono state completate
                            if (completedCount == queryset.size()) {
                                callback(data)
                            }
                        }

                    }
                    else {
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

    private fun popAdd(utente: Utente){

        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.pop_new_recenzione, null)
        val ratingbar= popupView.findViewById<RatingBar>(R.id.recenzione_ratingBar)
        val descrizione = popupView.findViewById<EditText>(R.id.Et_luogo)
        val BtnBack = popupView.findViewById<Button>(R.id.close_button)
        val BtnAdd = popupView.findViewById<Button>(R.id.add_botton)
        val alertDialogBuilder = AlertDialog.Builder(context).setView(popupView)
        val alertDialog = alertDialogBuilder.create()
        BtnBack.setOnClickListener {
            alertDialog.dismiss()
        }
        BtnAdd.setOnClickListener {
            var descrizione = descrizione.text.toString()
            var valore = ratingbar.rating
            if (descrizione.isEmpty() || valore.compareTo(0)==0 ||valore.compareTo(0.5)==0) {
                utils.PopError(
                    getString(R.string.Place_RecenzoneError_title),
                    getString(R.string.Place_RecenzioneError_text),
                    requireContext()
                )
            } else {
                var nome = utente.nome + " " + utente.cognome
                insert(luogo.id_luogo, descrizione, valore,utente.id,nome)
                alertDialog.dismiss()
            }
        }
        val builder = AlertDialog.Builder(context)
        builder.setView(popupView)

        //visualizza il pop-up
        alertDialog.show()
    }

    private fun insert(id_luogo: Int, descrizione: String, valutazione: Float,id_persona: Int,nome:String) {

        val currentDate = LocalDate.now()
        val query = "INSERT INTO recenzioni (id_luogo, descrizione,valutazione,id_persona,nome, data_publicazione) VALUES ('$id_luogo', '$descrizione', '$valutazione', '$id_persona','$nome','$currentDate');"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.insert(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server
                    Log.i("LOG", "Query creata:$query ")
                    if (response.isSuccessful) { //Se non ci sono stati errori di connessione con il server
                        loadRecyclerViewData()
                      //  utils.PopError(getString(R.string.register_new_account_title),getString(R.string.register_new_account_title),this@Registrazione)
                    } else {
                        Log.i("LOG", "Errore durante l'inserimento ")
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
}