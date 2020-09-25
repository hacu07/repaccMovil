package com.example.repacc.vehiculoAgregar.events
import com.example.repacc.pojo.Vehiculo
import com.example.repacc.util.Util
import java.io.Serializable

data class VehiculoEvent(
    var typeEvent: Int = Util.ERROR_CONEXION,
    val error: Boolean = true,
    val msj: String? = null,
    val content: Vehiculo? = null
): Serializable

