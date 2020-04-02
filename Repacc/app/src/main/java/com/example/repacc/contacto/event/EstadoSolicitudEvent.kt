package com.example.repacc.contacto.event

import com.example.repacc.pojo.Contacto
import com.example.repacc.util.Util

/*
* Respuesta de la API cuando el usuario ha aceptado o rechazado una solicitud
* */
data class EstadoSolicitudEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var content: Contacto? = null,
    val msj: String? = null
)