package com.example.progettowebemobile.entity

import java.io.Serializable


class Luogo(id_luogo: Int,nome: String, descrizione: String, numero_cellulare: Long, indirizzo: String,foto: String, valutazione: Float,luogo: String,tipo:String,sitoweb:String,come_arrivarci:String):
    Serializable {
    public var id_luogo: Int = id_luogo
     var nome: String = nome
     var descrizione: String = descrizione
     var numero_cellulare: Long = numero_cellulare
     var indirizzo: String = indirizzo
     var valutazione: Float = valutazione
     var foto: String = foto
     var luogo: String = luogo
    var tipo: String = tipo
     var sitoweb: String = sitoweb
    var come_arrivarci: String = come_arrivarci
}