package com.example.repacc.reportes.events

import com.example.repacc.pojo.Reporte
import com.example.repacc.util.Util
import java.io.Serializable

data class ReporteEvent(
    var typeEvent:Int = Util.ERROR_CONEXION,
    var error:Boolean = false,
    var msj:String? = null,
    var content: Reporte? = null
) : Serializable