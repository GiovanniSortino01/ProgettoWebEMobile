package com.example.progettowebemobile.entity

import java.util.Date


class Utente(id: Int,nome: String,cognome: String,data: Date,email: String, password: String) {
    private var id: Int = id
    private var nome: String = nome
    private var cognome: String = cognome
    private var data: Date = data
    private var email: String = email
    private var password: String = password
}