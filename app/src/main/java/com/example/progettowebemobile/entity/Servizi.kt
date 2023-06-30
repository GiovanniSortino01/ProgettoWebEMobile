package com.example.progettowebemobile.entity

import java.io.Serializable

class Servizi( id_luogo:Int, wifi:String, fitness:String, ciboebevande:String,  trasporti:String, generali:String,
               tipidicamere:String , servizioincamera:String,serviziopulizia:String,servizioreception:String)
    : Serializable {
        public var id_luogo: Int = id_luogo
        public var wifi: String = wifi
        public var fitness: String = fitness
        public var ciboebevande: String = ciboebevande
        public var trasporti: String = trasporti
        public var generali: String = generali
        public var tipidicamere: String = tipidicamere
        public var servizioincamera: String = servizioincamera
        public var servizioreception: String = servizioreception
        public var serviziopulizia: String = serviziopulizia
    }