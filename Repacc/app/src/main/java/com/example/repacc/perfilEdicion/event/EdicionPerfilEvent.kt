package com.example.repacc.perfilEdicion.event

import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Util

data class EdicionPerfilEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var content: Usuario? = null,
    val msj: String? = null
)
