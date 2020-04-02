package com.example.wcf.registro.event

import com.example.wcf.Util.Util
import com.example.wcf.pojo.Envios

data class RegistroEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    var error:Boolean = true,
    var msj: String? = null,
    var content: Envios? = null
)