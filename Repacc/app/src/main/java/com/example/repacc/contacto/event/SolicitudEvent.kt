package com.example.repacc.contacto.event

import com.example.repacc.pojo.Solicitud
import com.example.repacc.util.Util


data class SolicitudEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var content: ArrayList<Solicitud>? = null,
    val msj: String? = null
)