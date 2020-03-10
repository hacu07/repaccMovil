package com.example.repacc.perfilEdicion.event

import com.example.repacc.pojo.Pais
import com.example.repacc.util.Util

data class PaisEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    val content: List<Pais>? = null,
    val msj: String? = null
)