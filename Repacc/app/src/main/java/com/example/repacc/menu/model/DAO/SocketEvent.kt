package com.example.repacc.menu.model.DAO

import com.example.repacc.pojo.Auxiliares.SocketUsuario
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Util
import java.io.Serializable

data class SocketEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    val msj: String? = null,
    var socketUsuario: SocketUsuario? = null) : Serializable