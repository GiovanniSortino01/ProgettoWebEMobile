package com.example.progettowebemobile.principale

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.util.Log
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.progettowebemobile.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class GpsFragment : Fragment() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private val homeFragment: Fragment = HomeFragment() // Sostituisci con il tuo fragment Home

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gps, container, false)

        // Controlla se i permessi sono già stati concessi
        if (checkLocationPermission()) {
            openGoogleMaps()
        } else {
            // Richiedi i permessi
            requestLocationPermission()
        }
        navigateToHomeFragment()
        return view
    }

    private fun checkLocationPermission(): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val result = ContextCompat.checkSelfPermission(requireContext(), permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        if (shouldShowRequestPermissionRationale(permission)) {
            // Spiega all'utente perché sono necessari i permessi
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("I permessi di localizzazione sono necessari per utilizzare questa funzionalità.")
                .setTitle("Permessi Richiesti")
                .setPositiveButton("Concedi") { dialog, _ ->
                    dialog.dismiss()
                    requestPermissionLauncher.launch(permission)
                }
                .setNegativeButton("Annulla") { dialog, _ ->
                    dialog.dismiss()
                    navigateToHomeFragment()
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
                openGoogleMaps()
            } else {
                navigateToHomeFragment()
            }
        }

    private fun openGoogleMaps() {
        val latitude = 38.1157
        val longitude = 13.3613

        val uri = Uri.parse("geo:$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }





    /*
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double? = null
    private var longitude: Double? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gps, container, false)

        // Controlla se i permessi sono già stati concessi
        if (checkLocationPermission()) {
            openGoogleMaps()
        } else {
            // Richiedi i permessi
            requestLocationPermission()
        }
        navigateToHomeFragment()
        return view
    }

    private fun checkLocationPermission(): Boolean {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val result = ContextCompat.checkSelfPermission(requireContext(), permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        if (shouldShowRequestPermissionRationale(permission)) {
            // Spiega all'utente perché sono necessari i permessi
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setMessage("I permessi di localizzazione sono necessari per utilizzare questa funzionalità.")
                .setTitle("Permessi Richiesti")
                .setPositiveButton("Concedi") { dialog, _ ->
                    dialog.dismiss()
                    requestPermissionLauncher.launch(permission)
                }
                .setNegativeButton("Annulla") { dialog, _ ->
                    dialog.dismiss()
                    navigateToHomeFragment()
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
                openGoogleMaps()
            } else {
                navigateToHomeFragment()
            }
        }
*/
    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.action_gpsFragment_to_homeFragment)
    }
/*
    private fun openGoogleMaps() {
        val latitude = 38.1157
        val longitude = 13.3613

        val uri = Uri.parse("geo:$latitude,$longitude")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun openGoogleMaps(latitude: Double, longitude: Double) {
        val uri = Uri.parse("geo:${latitude ?: 0.0},${longitude ?: 0.0}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }


    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude
                        // Puoi utilizzare le coordinate qui come desiderato
                        openGoogleMaps(latitude!!, longitude!!)
                    } else {
                        // Non è stata trovata alcuna posizione disponibile
                        openGoogleMaps(38.1157, 13.3613)
                    }
                }
                .addOnFailureListener { exception ->
                    // Errore durante il recupero della posizione
                }
        }
    }*/
}

