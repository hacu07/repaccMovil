package com.example.repacc.perfilEdicion.model

import android.content.Context

interface PerfilEdicionModel {
    fun obtenerPaises(context: Context)

    fun obtenerDepartamentos(context: Context, idPais: String)

    fun obtenerCiudades(context: Context, idDpto: String)
}