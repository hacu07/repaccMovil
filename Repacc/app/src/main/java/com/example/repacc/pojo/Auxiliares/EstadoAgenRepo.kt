package com.example.repacc.pojo.Auxiliares

import com.example.repacc.pojo.Agente
import com.example.repacc.pojo.Entidad
import com.example.repacc.pojo.Estado
import com.example.repacc.pojo.Servicio
import java.io.Serializable

data class EstadoAgenRepo(
    val servicio: Servicio,
    val agente: Agente,
    val detalle: ArrayList<Detalle>,
    val estado: Estado,
    var unidadMedica: Entidad? = null,
    var descriptraslado: String? = null
    ): Serializable

data class Detalle(
    val estado: Estado,
    val latitud: Double,
    val longitud: Double
) : Serializable