package com.example.wcf.registro.event

import com.example.wcf.Util.Util
import com.example.wcf.pojo.Cliente

data class ClienEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    var error:Boolean = true,
    var msj:String?,
    var content: Cliente? = null
)