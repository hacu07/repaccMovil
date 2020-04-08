package com.example.wcf.pojo

import com.google.gson.annotations.SerializedName

data class Ciudad(
    @SerializedName("COD_CIUD") val cod_ciud: Int,
    @SerializedName("NOMBRE") val nombre: String
)