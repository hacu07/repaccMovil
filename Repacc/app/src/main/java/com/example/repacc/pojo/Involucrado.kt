package com.example.repacc.pojo

data class Involucrado(
    val _id : String,
    val reporte: Reporte,
    val tipo: Tipo,
    val placaqr: String,
    val usuaInvolucrado: Usuario
)