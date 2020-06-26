package com.example.repacc.pojo

import java.io.Serializable

data class Estado(
    var _id: String? = null,
    var nombre: String? = null,
    var tipo: Int = 0,
    val orden: Int = 0,
    var descripcion: String? = null,
    val codigo: String? = null
    ) : Serializable