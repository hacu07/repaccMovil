package com.example.wcf.pojo

import com.google.gson.annotations.SerializedName

data class Cliente(
    @SerializedName("CEDULA") val cedula: String? = null,
    @SerializedName("NOMBRES") var nombres: String?= null,
    @SerializedName("APELLIDOS") var apellidos: String? = null,
    @SerializedName("FECHA_NAC") var fecha_nac: String? = null,
    @SerializedName("DIRECCION") var direccion: String? = null,
    @SerializedName("TELEFONO") var telefono: String? = null,
    @SerializedName("EMAIL") var email: String? = null,
    @SerializedName("FECHA_REG") var fecha_reg: String? = null,
    @SerializedName("ESTADO") var estado:Int = 1
)