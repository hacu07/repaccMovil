package com.example.wcf.pojo

import com.google.gson.annotations.SerializedName

data class Pesos(
    @SerializedName("CODIGO") val codigo: Int,
    @SerializedName("CIUDAD_DEST") val ciud_orig: Ciudad,
    @SerializedName("CIUDAD_ORIG") val ciud_dest: Ciudad,
    @SerializedName("PRECIOKL") val preciokl: Int
)