package com.example.repacc.login.event

import com.example.repacc.pojo.Agente
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Util

data class LoginEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    var content: Usuario? = Usuario(),
    val agente: Agente? = null,
    val msj: String
)