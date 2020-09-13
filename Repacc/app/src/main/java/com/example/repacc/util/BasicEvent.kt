package com.example.repacc.util

import java.io.Serializable

open class BasicEvent(
    open var typeEvent: Int = Util.ERROR_CONEXION,
    open val error: Boolean = true,
    open val msj: String? = null
): Serializable