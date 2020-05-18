package com.example.repacc.pojo

import java.io.Serializable

data class Rol(
    var _id: String? = null,
    var nombre: String? = null,
    var tipo: Int? = 0,
    var estado:Estado? = null
) : Serializable