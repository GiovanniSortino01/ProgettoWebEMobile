package com.example.progettowebemobile.entity

import java.io.Serializable

class Persona (id: Int,nome: String,cognome: String,data: String, immagine: String) :
        Serializable {
        public var id: Int = id
        public var nome: String = nome
        public var cognome: String = cognome
        public var data: String = data
        public var immagine: String = immagine
    }