package com.example.repacc.perfilEdicion.event

import com.example.repacc.pojo.Municipio
import com.example.repacc.util.Util

data class MunicipioEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    val content: List<Municipio>? = null,
    val msj: String? = null
)