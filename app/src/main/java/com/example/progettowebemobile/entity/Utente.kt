package com.example.progettowebemobile.entity

import java.io.Serializable
import java.util.Date


class Utente(id: Int,nome: String,cognome: String,data: String,email: String, password: String, immagine: String) :
    Serializable {
    public var id: Int = id
    public var nome: String = nome
    public var cognome: String = cognome
    public var data: String = data
    public var email: String = email
    public var password: String = password
    public var immagine: String = immagine
}