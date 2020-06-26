package com.example.repacc.pojo

import java.util.*

data class Detalle(
    val _id : String,
    val estado: Estado,
    val latitud: Double = 0.0,
    val longitud: Double= 0.0,
    val date: Date ?  = null
)
