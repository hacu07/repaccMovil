package com.example.repacc.contactoAgregar.events

import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Util

data class ContactosEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var content: ArrayList<Usuario>? = null,
    val msj: String? = null
)