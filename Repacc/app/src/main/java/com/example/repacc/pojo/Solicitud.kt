package com.example.repacc.pojo

data class Solicitud(
    val _id: String,
    val usuario: Usuario,
    val contacto: Usuario,
    val estado: Estado
)