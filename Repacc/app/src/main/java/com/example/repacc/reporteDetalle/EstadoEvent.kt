package com.example.repacc.reporteDetalle

import com.example.repacc.pojo.Estado
import com.example.repacc.util.Util

data class EstadoEvent(
    var typeEvent:Int = Util.ERROR_CONEXION,
    var error:Boolean = false,
    var msj:String? = null,
    var content: ArrayList<Estado>? = null
)