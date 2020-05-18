package com.example.repacc.vehiculoAgregar.events

import com.example.repacc.pojo.Marca
import com.example.repacc.util.Util

data class MarcaEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var content: ArrayList<Marca>? = null,
    val msj: String? = null
)
