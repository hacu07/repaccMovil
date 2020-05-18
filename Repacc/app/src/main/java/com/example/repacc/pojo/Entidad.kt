package com.example.repacc.pojo

import java.io.Serializable

data class Entidad(
    val _id: String,
    val nit: String,
    val nombre: String,
    val direccion: String,
    val municipio: Municipio,
    val latlong: String,
    val latitud: Double,
    val longitud: Double,
    val estado: Estado,
    val tipoEntidad: Tipo
) : Serializable