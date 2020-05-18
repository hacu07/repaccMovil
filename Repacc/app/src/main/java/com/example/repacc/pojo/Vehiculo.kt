package com.example.repacc.pojo

data class Vehiculo(
    val _id: String? = null,
    val foto: String? = null,
    val usuario: Usuario? = null,
    val tipo: Tipo,
    val esParticular: Boolean,
    val modelo: Modelo,
    val colores: Array<String>,
    val placa: String
)