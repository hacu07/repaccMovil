package com.example.repacc.vehiculoAgregar.events

import com.example.repacc.pojo.Modelo
import com.example.repacc.util.Util

data class ModeloEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var content: ArrayList<Modelo>? = null,
    val msj: String? = null
)
