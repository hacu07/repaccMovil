package com.example.wcf.cliente.event

import com.example.wcf.Util.Util
import com.example.wcf.pojo.Cliente

data class ClienteEvent(
    var event: Int =  Util.CLIENTE_EVENT_REGISTRO,
    var typeEvent: Int = Util.ERROR_CONEXION,
    var error:Boolean = true,
    var msj:String?,
    var content: Cliente? = null
)