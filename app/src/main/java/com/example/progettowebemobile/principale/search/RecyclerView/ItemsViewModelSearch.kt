package com.example.progettowebemobile.principale.search.RecyclerView

import android.graphics.Bitmap

data class ItemsViewModelSearch(val image: Bitmap?, val id:Int, val name: String, val luogo: String, val recenzione: Float, val tipo:String, val sito: String, val come_arrviarci: String,
                                var preferito:Boolean)