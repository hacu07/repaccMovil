package com.example.repacc.pojo

import java.io.Serializable

data class Agente(
    val _id: String,
    val usuario: Usuario,
    val estado: Estado,
    val munServicio: Municipio,
    val entidad: Entidad
) : Serializable