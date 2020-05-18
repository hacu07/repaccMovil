package com.example.repacc.vehiculo.events

import com.example.repacc.pojo.Vehiculo
import com.example.repacc.util.Util

data class VehiculoEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var content: ArrayList<Vehiculo>? = null,
    val msj: String? = null
)
