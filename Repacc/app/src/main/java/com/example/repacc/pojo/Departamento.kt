package com.example.repacc.pojo

data class Departamento(
    val _id: String,
    val codigo: String,
    val nombre: String,
    val pais: Pais,
    val estado: Estado
)