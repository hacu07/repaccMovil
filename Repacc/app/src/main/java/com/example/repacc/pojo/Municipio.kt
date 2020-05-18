package com.example.repacc.pojo

import java.io.Serializable

data class Municipio(
    val _id: String,
    val codigo: String,
    val capital: Boolean,
    val nombre: String,
    val departamento: Departamento,
    val estado: Estado
): Serializable