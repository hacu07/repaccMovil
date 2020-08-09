package com.example.repacc.reporteDetalle.view.fragments.events

import com.example.repacc.pojo.Entidad
import com.example.repacc.util.Util
import java.io.Serializable

data class EntidadesEvent (
    var typeEvent:Int = Util.ERROR_CONEXION,
    var error:Boolean = false,
    var msj:String? = null,
    var content:ArrayList<Entidad>? = null
): Serializable