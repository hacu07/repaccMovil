package com.example.wcf.pojo

import com.google.gson.annotations.SerializedName

data class Envios(
     @SerializedName("COD_GUIA") val cod_guia: Int = 0,
     @SerializedName("CEDULA_EMI") val cedula_emi: Cliente,
     @SerializedName("CEDULA_DES") val cedula_des: Cliente,
     @SerializedName("CIUD_ORIG") val ciud_orig: Ciudad,
     @SerializedName("CIUD_DEST") val ciud_dest: Ciudad,
     @SerializedName("PESO") val peso :Int,
     @SerializedName("VALOR_ASEG") val valor_aseg: Int,
     @SerializedName("PRECIOKL") val preciokl: Int,
     @SerializedName("VALOR_ENVI") val valor_envi: Int,
     @SerializedName("COD_ESTADO") val cod_estado: Estados = Estados(cod_estado = 1, descripcion = "PENDIENTE")
)