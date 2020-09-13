package com.example.repacc.pojo.Auxiliares

import java.io.Serializable

/*************************
 * Se utiliza para actualizar el socket del usuario en la BD
 * HAROLDC 23/08/2020
 */
data class SocketUsuario(
    val _id: String,
    val asignar: Boolean,
    val socketId: String? = null
): Serializable