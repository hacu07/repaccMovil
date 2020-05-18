package com.example.repacc.pojo

import java.io.Serializable

data class Departamento(
    val _id: String,
    val codigo: String,
    val nombre: String,
    val pais: Pais,
    val estado: Estado
) : Serializable