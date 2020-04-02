package com.example.repacc.contacto.event

import com.example.repacc.pojo.Contacto
import com.example.repacc.util.Util

data class ContactoEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var content: ArrayList<Contacto>? = null,
    val msj: String? = null
)