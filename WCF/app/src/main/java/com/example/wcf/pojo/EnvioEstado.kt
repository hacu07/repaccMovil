package com.example.wcf.pojo

import com.google.gson.annotations.SerializedName

data class EnvioEstado(
    @SerializedName("COD_GUIA") val cod_guia :Int,
    @SerializedName("COD_ESTADO") val cod_estado: Int
)