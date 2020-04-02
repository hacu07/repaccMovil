package com.example.wcf.pojo

data class Cliente(
    val cedula: String? = null,
    var nombres: String?= null,
    var apellidos: String? = null,
    var fecha_nac: String? = null,
    var direccion: String? = null,
    var telefono: String? = null,
    var email: String? = null,
    var fecha_reg: String? = null,
    var estado:Int = 1
)