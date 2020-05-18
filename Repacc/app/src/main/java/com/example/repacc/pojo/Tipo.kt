package com.example.repacc.pojo

import java.io.Serializable

data class Tipo(
    val _id: String,
    val tipo: Int,
    val codigo: String,
    val nombre: String,
    val estado: Estado,
    val descripcion: String? = null
) : Serializable