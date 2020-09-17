package com.example.repacc.pojo

import java.io.Serializable
import java.util.*

data class Notificacion(
    val _id : String,
    val reporte: Reporte,
    // Se establece cuando se envia al contacto del involucrado
    val involucrado: Involucrado? = null,
    val mensaje: String,
    val tipo: Tipo,
    // usuario al que se le envia el mensaje
    val usuario: Usuario,
    val rol: Rol, // Rol del usuario destino
    val estado: Estado,
    val createdAt: Date
): Serializable