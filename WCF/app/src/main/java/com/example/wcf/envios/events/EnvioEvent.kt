package com.example.wcf.envios.events

import com.example.wcf.Util.Util
import com.example.wcf.pojo.Envios

data class EnvioEvent (
    var typeEvent: Int = Util.ERROR_CONEXION,
    var msj: String? = null,
    val error: Boolean = true,
    var content: Envios? = null
)