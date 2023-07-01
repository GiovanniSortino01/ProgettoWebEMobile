package com.example.progettowebemobile

import com.example.progettowebemobile.entity.Utente

class Buffer {
    companion object {
        var utente: Utente? = null
        var prezzo: Int? = null
    }

    fun getUtente(): Utente? {
        return utente
    }

    fun setUtente(utente: Utente) {
        Companion.utente = utente
    }
    fun getPrezzo(): Int? {
        return prezzo
    }


    fun setPrezzo(utente: Int) {
        Companion.prezzo = prezzo
    }

}