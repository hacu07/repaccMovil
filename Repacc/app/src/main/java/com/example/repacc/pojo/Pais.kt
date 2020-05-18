package com.example.repacc.pojo

import java.io.Serializable

data class Pais (
    val _id: String,
    val codigo: String,
    val prefijoTel: String,
    val nombre: String,
    val estado: Estado
) : Serializable