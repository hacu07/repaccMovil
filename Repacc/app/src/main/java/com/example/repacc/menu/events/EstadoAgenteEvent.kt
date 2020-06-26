package com.example.repacc.menu.events

import com.example.repacc.pojo.Estado
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Util

data class EstadoAgenteEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    val msj: String? = null,
    val content: Estado
)