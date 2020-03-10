package com.example.repacc.pojo

data class Usuario(
    var _id:String? = null,
    var qr:String? = null,
    var correo:String? = null,
    var nombre:String? = null,
    var contrasena:String? = null,
    var celular:String? = null,
    var usuario:String? = null,
    var codRecuCon:String? = null,
    var date: String? = null,
    var rol: Rol? = null,
    var foto: String? = null,
    var tipoSangre: String? = null,
    var munNotif: Municipio? = null,
    var recibirNotif: Boolean? = null,
    var munResid: Municipio? = null,
    var estado: Estado? = null
    )