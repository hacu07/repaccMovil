package com.example.wcf.envios.events

import com.example.wcf.Util.Util

data class EstadoEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var msj: String? = null
)