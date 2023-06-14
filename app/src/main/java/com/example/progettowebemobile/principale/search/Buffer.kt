package com.example.progettowebemobile.principale.search

import com.example.progettowebemobile.entity.Utente

class Buffer {
    companion object {
        var utente: Utente? = null
    }

    fun getUtente(): Utente? {
        return utente
    }

    fun setUtente(utente: Utente) {
        Buffer.utente = utente
    }
}