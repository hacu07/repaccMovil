package com.example.repacc.pojo

data class AgenRepo(
    val _id: String,
    val servicio: Servicio,
    val agente: Agente,
    val detalle: ArrayList<Detalle>,
    val estado: Estado,
    val descriptraslado : String? = null,
    val unidadMedica: Entidad? = null
)