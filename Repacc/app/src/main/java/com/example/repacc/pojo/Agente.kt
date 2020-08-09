package com.example.repacc.pojo

import java.io.Serializable

data class Agente(
    val _id: String,
    var ocupado: Boolean = false,
    val usuario: Usuario,
    var estado: Estado,
    val municipio: Municipio,
    val entidad: Entidad,
    var latitud: Double? = 0.0,
    var longitud: Double? = 0.0,
    val servicio: String,
    var disponible: Boolean = false // Usado al consumir serice de actualizar estado o disponibilidad
) : Serializable