package com.example.progettowebemobile.principale

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.example.progettowebemobile.R
import com.example.progettowebemobile.databinding.ActivityMainBinding

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Esegui le azioni desiderate qui
                // ad esempio, torna indietro o chiudi il Fragment
                showDialogToConfirmExit()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
        return inflater.inflate(R.layout.fragment_home, container, false)
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
}