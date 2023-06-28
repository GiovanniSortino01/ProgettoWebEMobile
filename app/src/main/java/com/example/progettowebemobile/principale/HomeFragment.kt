package com.example.progettowebemobile.principale

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.ActivityMainBinding
import com.example.progettowebemobile.databinding.FragmentAccountBinding
import com.example.progettowebemobile.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Esegui le azioni desiderate qui
                // ad esempio, torna indietro o chiudi il Fragment
                showDialogToConfirmExit()
            }
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView


        toggle = ActionBarDrawerToggle(requireActivity(), drawerLayout, R.string.home_nav_open, R.string.home_nav_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        navView.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.nav_prenotazioni -> Toast.makeText(
                    requireContext(),
                    "Clicked Prenotazioni",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.nav_preferiti -> Toast.makeText(
                    requireContext(),
                    "Clicked Preferti",
                    Toast.LENGTH_SHORT
                ).show()

                R.id.nav_chiSiamo -> Toast.makeText(
                    requireContext(),
                    "Clicked Chi siamo",
                    Toast.LENGTH_SHORT
                ).show()
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
}