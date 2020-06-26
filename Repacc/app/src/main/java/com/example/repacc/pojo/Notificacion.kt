package com.example.repacc.pojo

data class Notificacion(
    val _id : String,
    val reporte: Reporte,
    val involucrado: Involucrado,
    val mensaje: String,
    val tipo: Tipo,
    val usuario: Usuario, // destino del mensaje
    val rol: Rol, // Rol del usuario destino
    val estado: Estado
)