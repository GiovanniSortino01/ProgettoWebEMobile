package com.example.progettowebemobile

import android.app.AlertDialog
import android.content.Context

class Utils {
    fun PopError(titolo:String, frase: String, context: Context){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(frase)
            .setTitle(titolo)
            .setPositiveButton("Chiudi") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

}