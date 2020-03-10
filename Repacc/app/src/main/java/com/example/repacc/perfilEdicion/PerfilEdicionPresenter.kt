package com.example.repacc.perfilEdicion

import android.content.Context

interface PerfilEdicionPresenter {
    fun onCreate()
    fun onDestroy()

    fun obtenerPaises(context: Context)
    fun obtenerDepartamentos(context: Context, idPais: String)
    fun obtenerMunicipios(context: Context, idDpto: String)
}