package com.example.repacc.menu.events

import com.example.repacc.pojo.Notificacion
import com.example.repacc.util.Util
import java.io.Serializable

class NotificacionesEvent (
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    val msj: String? = null,
    val content: ArrayList<Notificacion>? = null
) : Serializable