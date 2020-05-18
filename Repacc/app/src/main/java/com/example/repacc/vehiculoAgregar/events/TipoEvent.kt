package com.example.repacc.vehiculoAgregar.events

import com.example.repacc.pojo.Tipo
import com.example.repacc.util.Util

data class TipoEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var content: ArrayList<Tipo>? = null,
    val msj: String? = null
)