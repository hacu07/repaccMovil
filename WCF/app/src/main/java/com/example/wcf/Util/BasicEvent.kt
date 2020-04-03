package com.example.wcf.Util

data class BasicEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    val msj: String? = null
)