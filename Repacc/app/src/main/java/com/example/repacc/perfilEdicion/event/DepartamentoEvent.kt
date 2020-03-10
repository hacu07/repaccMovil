package com.example.repacc.perfilEdicion.event

import com.example.repacc.pojo.Departamento
import com.example.repacc.util.Util

class DepartamentoEvent (
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var content: List<Departamento>? = null,
    val msj: String? = null
)