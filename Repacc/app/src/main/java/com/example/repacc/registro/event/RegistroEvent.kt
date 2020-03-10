package com.example.repacc.registro.event

import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Util
import java.io.Serializable

data class RegistroEvent(
    var typeEvent:Int = Util.ERROR_CONEXION,
    var error:Boolean = false,
    var msj:String? = null,
    var content:Usuario? = null
)
