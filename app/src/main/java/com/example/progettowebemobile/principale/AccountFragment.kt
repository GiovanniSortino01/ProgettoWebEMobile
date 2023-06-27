package com.example.progettowebemobile.principale

import android.app.AlertDialog
import android.content.ContentValues
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.entity.Utente
import com.example.progettowebemobile.Buffer
import com.example.progettowebemobile.R
import com.example.progettowebemobile.Utils
import com.example.progettowebemobile.databinding.FragmentAccountBinding
import com.example.progettowebemobile.principale.account.ItemsViewModelPost
import com.example.progettowebemobile.principale.account.PersonalAccountAdapter
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.time.LocalDate

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var utente: Utente
    private var utils = Utils()
    private var buffer = Buffer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val intent = Intent()
        utente = buffer.getUtente()!!

        binding = FragmentAccountBinding.inflate(inflater, container, false)

        binding.userRecycleView.layoutManager = LinearLayoutManager(requireContext())

        val nomeUtente = utente.nome
        val cognomeUtente = utente.cognome
        val dataInscrizioneUtente = utente?.data

        binding.userName.text = nomeUtente
        binding.userSurname.text = cognomeUtente
        binding.userRegistrationDate.text = dataInscrizioneUtente
        getImageProfilo(utente.immagine)

        loadRecyclerViewData()

        binding.userBtnEdit.setOnClickListener {
            popEdit()
        }
        binding.btnAddPost.setOnClickListener {
            popAdd()
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Esegui le azioni desiderate qui
                // ad esempio, torna indietro o chiudi il Fragment
                findNavController().navigate(R.id.action_accountFragment_to_homeFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        return binding.root
    }

    private fun loadRecyclerViewData() {
        getItems(utente.id) { data ->
            val adapter = PersonalAccountAdapter(data)
            binding.userRecycleView.adapter = adapter

            adapter.setOnClickListener(object : PersonalAccountAdapter.OnClickListener {
                override fun onClick(position: Int, model: ItemsViewModelPost) {
                    Log.i(ContentValues.TAG, "Index ${position + 1}")
                }
            })
            adapter.notifyDataSetChanged() // Aggiungi questa linea per aggiornare l'adapter
        }
    }

    private fun getImageProfilo(url: String) {
        ClientNetwork.retrofit.getAvatar(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        var avatar: Bitmap? = null
                        if (response.body() != null) {
                            avatar = BitmapFactory.decodeStream(response.body()?.byteStream())
                            binding.imageView2.setImageBitmap(avatar)
                        }
                    }
                }

                override fun onFailure(
                    call: Call<ResponseBody>,
                    t: Throwable
                ) { //Questo metodo viene chiamato quando si verifica un errore durante la chiamata HTTP.
                }
            }
        )
    }

    private fun getItems(id: Int, callback: (ArrayList<ItemsViewModelPost>) -> Unit) {
        val query = "select * from post where id_persona = '$id';"
        val data = ArrayList<ItemsViewModelPost>()

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    val queryset = response.body()?.getAsJsonArray("queryset")

                    if (queryset?.size()!! >= 1) {

                        var completedCount =
                            0 // Contatore per tenere traccia del numero di chiamate completate

                        for (i in 0 until queryset.size()) {
                            val item = queryset.get(i).asJsonObject
                            var foto = item.get("foto").asString
                            getImageProfiloRecyclerView(foto) { avatar ->
                                completedCount++
                                if (avatar != null) {
                                    data.add(
                                        ItemsViewModelPost(
                                            item.get("id_post").asInt,
                                            utente.nome,
                                            utente.cognome,
                                            avatar,
                                            item.get("descrizione").asString,
                                            item.get("luogo").asString,
                                            item.get("data").asString
                                        )
                                    )
                                }
                                // Verifica se tutte le chiamate sono state completate
                                if (completedCount == queryset.size()) {
                                    callback(data)
                                }
                            }
                        }
                    } else {
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

    private fun getImageProfiloRecyclerView(url: String, callback: (Bitmap?) -> Unit) {
        ClientNetwork.retrofit.getAvatar(url).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val bitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
                    callback(bitmap)
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback(null)
            }
        })
    }

    private fun insert(id: Int, descrizione: String, luogo: String) {

        val currentDate = LocalDate.now()
        val query =
            "INSERT INTO post (id_persona, descrizione , luogo, data) VALUES ('${id}', '${descrizione}', '${luogo}', '$currentDate');"
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

    private fun update(id: Int, nome: String, cognome: String, email: String) {


        val query =
            "UPDATE utenti SET nome = '$nome', cognome = '$cognome', email = '$email' WHERE id = $id;"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.update(query).enqueue(
            object : Callback<JsonObject> {


                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server

                    Log.i("LOG", "Query creata:$query ")
                    if (response.isSuccessful) { //Se non ci sono stati errori di connessione con il server
                        utente.nome = nome
                        utente.cognome = cognome
                        utente.email = email
                    } else {
                        Log.i("LOG", "Errore durante la modifica ")
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

    private fun updatePassword(id: Int, password: String) {
        val query = "UPDATE utenti SET password = '$password' WHERE id = $id;"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.update(query).enqueue(
            object : Callback<JsonObject> {


                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {// Questo metodo viene chiamato quando la risposta HTTP viene ricevuta con successo dal server

                    Log.i("LOG", "Query creata:$query ")
                    if (response.isSuccessful) { //Se non ci sono stati errori di connessione con il server
                        utente.password = password
                    } else {
                        Log.i("LOG", "Errore durante la modifica ")
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
    private fun popEdit() {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.pop_up_edit, null)
        val popupButtonClose = popupView.findViewById<Button>(R.id.user_edit_btnBack)
        val popupButtonEdit = popupView.findViewById<Button>(R.id.user_edit_btnEdit)
        val popupButtonEditPhoto = popupView.findViewById<Button>(R.id.user_edit_btnPhoto)
        val popupButtonEditPassword = popupView.findViewById<Button>(R.id.user_edit_btnPassword)
        var Et_nome = popupView.findViewById<EditText>(R.id.user_edit_etName)
        var Et_cognome = popupView.findViewById<EditText>(R.id.user_edit_etSurname)
        var Et_email = popupView.findViewById<EditText>(R.id.user_edit_etEmail)
        val alertDialogBuilder = AlertDialog.Builder(context).setView(popupView)
        val alertDialog = alertDialogBuilder.create()

        Et_nome.setText(utente.nome)
        Et_cognome.setText(utente.cognome)
        Et_email.setText(utente.email)

        popupButtonClose.setOnClickListener {
            alertDialog.dismiss()
        }
        popupButtonEdit.setOnClickListener {
            var nome = Et_nome.text
            var cognome = Et_cognome.text
            var email = Et_email.text
            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty()) {
                utils.PopError(
                    getString(R.string.user_error_popup_edit_title),
                    getString(R.string.user_error_popup_edit_text),
                    requireContext()
                )
            } else {
                update(utente.id, nome.toString(), cognome.toString(), email.toString())
                utils.PopError(
                    getString(R.string.user_edit_modificaEffetuata_title),
                    getString(R.string.user_edit_modificaEffetuata_text),
                    requireContext()
                )
                alertDialog.dismiss()
            }
        }



        popupButtonEditPhoto.setOnClickListener {
            val popupView = inflater.inflate(R.layout.pop_up_photo, null)
            val popupButtonGalley = popupView.findViewById<Button>(R.id.btn_galley)
            val popupButtonCamera = popupView.findViewById<Button>(R.id.btn_camera)
            val alertDialogBuilder = AlertDialog.Builder(context).setView(popupView)
            val alertDialog2 = alertDialogBuilder.create()
            val builder = AlertDialog.Builder(context)

            popupButtonGalley.setOnClickListener{
                if (checkGalleryPermission()) {
                   // apri galleria
                    openGallery()
                } else {
                    // Richiedi i permessi
                    requestGalleryPermission()
                }
            }

            popupButtonCamera.setOnClickListener{
                if (checkCameraPermission()) {
                    openCamera()
                } else {
                    // Richiedi i permessi
                    requestCameraPermission()
                }
            }

            builder.setView(popupView)
            alertDialog2.show()
        }



        popupButtonEditPassword.setOnClickListener {
            alertDialog.dismiss()
            val popupView = inflater.inflate(R.layout.pop_up_edit_password, null)
            val popupButtonClose = popupView.findViewById<Button>(R.id.user_edit_btnBack)
            val popupButtonEditPassword = popupView.findViewById<Button>(R.id.user_edit_btnPassword)
            var Et_oldPassword = popupView.findViewById<EditText>(R.id.user_edit_etOldPassword)
            var Et_newPassword = popupView.findViewById<EditText>(R.id.user_edit_etNewPassword)
            var Et_confermaPassword =
                popupView.findViewById<EditText>(R.id.user_edit_etConfermaPassword)
            val alertDialogBuilder = AlertDialog.Builder(context).setView(popupView)
            val alertDialog2 = alertDialogBuilder.create()
            popupButtonClose.setOnClickListener {
                alertDialog2.dismiss()
                alertDialog.show()
            }
            popupButtonEditPassword.setOnClickListener {
                var oldPassword = Et_oldPassword.text.toString()
                var newPassword = Et_newPassword.text.toString()
                var confermaPassword = Et_confermaPassword.text.toString()
                var password = utente.password

                if (oldPassword.isEmpty() || newPassword.isEmpty() || confermaPassword.isEmpty()) {
                    utils.PopError(
                        getString(R.string.user_error_popup_editPassword_title),
                        getString(R.string.user_error_popup_editPassword_text),
                        requireContext()
                    )
                } else if (!oldPassword.equals(password)) {
                    utils.PopError(
                        getString(R.string.user_error_popup_editPassword_noConform_title),
                        getString(R.string.user_error_popup_editPassword_noConform_text),
                        requireContext()
                    )
                } else if (!newPassword.equals(confermaPassword)) {
                    utils.PopError(
                        getString(R.string.user_error_popup_editPassword_passwordDiverse_title),
                        getString(R.string.user_error_popup_editPassword_passwordDiverse_text),
                        requireContext()
                    )
                } else {
                    updatePassword(utente.id, newPassword.toString())
                    alertDialog2.dismiss()
                    alertDialog.show()
                    utils.PopError(
                        getString(R.string.user_edit_cambioEffetuato_title),
                        getString(R.string.user_edit_cambioEffetuato_text),
                        requireContext()
                    )

                }
            }
            val builder = AlertDialog.Builder(context)
            builder.setView(popupView)
            //visualizza il pop-up
            alertDialog2.show()
        }

        val builder = AlertDialog.Builder(context)
        builder.setView(popupView)
        //visualizza il pop-up
        alertDialog.show()
    }
    private fun popAdd(){

        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.pop_up_post, null)
        val popupButtonClose = popupView.findViewById<Button>(R.id.close_button)
        val popupButtonAdd = popupView.findViewById<Button>(R.id.add_botton)
        val Et_descrizione = popupView.findViewById<EditText>(R.id.Et_descrizione)
        val Et_luogo = popupView.findViewById<EditText>(R.id.Et_luogo)
        val alertDialogBuilder = AlertDialog.Builder(context).setView(popupView)
        val alertDialog = alertDialogBuilder.create()
        popupButtonClose.setOnClickListener {
            alertDialog.dismiss()
        }
        popupButtonAdd.setOnClickListener {
            var descrizione = Et_descrizione.text.toString()
            var luogo = Et_luogo.text.toString()
            if (descrizione.isEmpty() || luogo.isEmpty()) {
                utils.PopError(
                    getString(R.string.user_error_popup_title),
                    getString(R.string.user_error_popup_text),
                    requireContext()
                )
            } else {
                insert(utente.id, descrizione, luogo)
                alertDialog.dismiss()
            }
        }
        val builder = AlertDialog.Builder(context)
        builder.setView(popupView)

        //visualizza il pop-up
        alertDialog.show()
    }

    private fun checkGalleryPermission(): Boolean {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        val result = ContextCompat.checkSelfPermission(requireContext(), permission)
        return result == PackageManager.PERMISSION_GRANTED
    }
    private fun requestGalleryPermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE

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
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openGallery()
            } else {
                // Permesso negato, gestisci di conseguenza
                // Ad esempio, mostra un messaggio all'utente o esegui un'azione alternativa
            }
        }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    private fun checkCameraPermission(): Boolean {
        val permission = Manifest.permission.CAMERA
        val result = ContextCompat.checkSelfPermission(requireContext(), permission)
        return result == PackageManager.PERMISSION_GRANTED
    }
    private fun requestCameraPermission() {
        val permission = Manifest.permission.CAMERA

        if (shouldShowRequestPermissionRationale(permission)) {
            // Spiega all'utente perché sono necessari i permessi
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage(getString(R.string.dialog_text))
                .setTitle(getString(R.string.dialog_title))
                .setPositiveButton(R.string.dialog_concedi) { dialog, _ ->
                    dialog.dismiss()
                    requestPermissionLauncher2.launch(permission)
                }
                .setNegativeButton(R.string.dialog_annulla) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            // Richiedi direttamente i permessi
            requestPermissionLauncher2.launch(permission)
        }
    }

    private val requestPermissionLauncher2 =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Il permesso è stato concesso
                openCamera()
            } else {
                // Il permesso è stato negato
                // Puoi gestire il caso in cui il permesso è stato negato
            }
        }
    private fun openCamera() {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, 1)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.imageView2.setImageBitmap(imageBitmap)
        }else if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            if (data != null) {
                val selectedImage: Uri? = data.data
                if (selectedImage != null) {
                    val imageBitmap = getBitmapFromUri(selectedImage)
                    binding.imageView2.setImageBitmap(imageBitmap)
                }
            }
        }
    }
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

}
