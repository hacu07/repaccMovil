package com.example.repacc.pojo

import java.io.Serializable

data class Servicio(
    val _id: String,
    val reporte: Reporte? = null,
    val tipo: Tipo,
    val estado: Estado
): Serializable