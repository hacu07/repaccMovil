package com.example.wcf.pojo

import com.google.gson.annotations.SerializedName

data class Estados(
    @SerializedName("COD_ESTADO") val cod_estado: Int,
    @SerializedName("DESCRIPCION") val descripcion: String
)