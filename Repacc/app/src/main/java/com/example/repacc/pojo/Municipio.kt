package com.example.repacc.pojo

data class Municipio(
    val _id: String,
    val codigo: String,
    val capital: Boolean,
    val nombre: String,
    val departamento: Departamento,
    val estado: Estado
)