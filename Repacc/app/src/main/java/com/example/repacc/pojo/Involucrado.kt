package com.example.repacc.pojo

import java.io.Serializable

data class Involucrado(
    val _id : String,
    val reporte: Reporte,
    val tipo: Tipo,
    val placaqr: String,
    val usuaInvolucrado: Usuario
): Serializable