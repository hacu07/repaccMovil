package com.example.wcf.registro.event

import com.example.wcf.Util.Util
import com.example.wcf.pojo.Pesos

data class PesosEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    var error:Boolean = true,
    var msj: String? = null,
    var content: ArrayList<Pesos>? = null
)