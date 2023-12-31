package com.example.progettowebemobile.principale

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.example.db_connection.ClientNetwork
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.ActivityMainBinding
import com.example.progettowebemobile.databinding.FragmentAccountBinding
import com.example.progettowebemobile.databinding.FragmentHomeBinding
import com.example.progettowebemobile.login.Login
import com.example.progettowebemobile.login.Registrazione
import com.google.android.material.navigation.NavigationView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var intent: Intent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val menuIcon: ImageView = binding.menuIcon

        toggle = ActionBarDrawerToggle(requireActivity(), drawerLayout, R.string.home_nav_open, R.string.home_nav_close)
        drawerLayout.addDrawerListener(toggle)

        menuIcon.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        toggle.syncState()
        
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Esegui le azioni desiderate qui
                // ad esempio, torna indietro o chiudi il Fragment
                drawerLayout.closeDrawer(GravityCompat.START)
                showDialogToConfirmExit()
            }
        }


        navView.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_prenotazioni ->
                    findNavController().navigate(R.id.action_homeFragment_to_prenotazioniFragment2)


                R.id.nav_pagamenti ->
                    findNavController().navigate(R.id.action_homeFragment_to_pagamentiFragment)

                R.id.nav_preferiti ->
                    findNavController().navigate(R.id.action_homeFragment_to_preferitiFragment)

                R.id.nav_logout -> {
                    val dialogBuilder = android.app.AlertDialog.Builder(requireContext())
                dialogBuilder.setMessage(getString(R.string.log_out_text))
                    .setTitle(getString(R.string.log_out_title))
                    .setPositiveButton(R.string.log_out_yes) { dialog, _ ->
                        dialog.dismiss()
                        intent = Intent(requireContext(), Login::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    .setNegativeButton(R.string.log_out_no) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()}
            }
            true
        }

        //fine Navigation Drawer

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = requireActivity().actionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }


    private fun showDialogToConfirmExit() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.back_title))
        builder.setMessage(getString(R.string.back_text))
        builder.setPositiveButton(getString(R.string.back_yes)) { dialog, which ->
            requireActivity().finish()
        }
        builder.setNegativeButton(getString(R.string.back_no)) { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }


    //funzione Navigation Drawer
     override fun onOptionsItemSelected(item: MenuItem): Boolean {
          if (toggle.onOptionsItemSelected(item)){
              return true}

          return super.onOptionsItemSelected(item)
      }

    private fun getImage(url: String, callback: (Bitmap?) -> Unit) {
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
}