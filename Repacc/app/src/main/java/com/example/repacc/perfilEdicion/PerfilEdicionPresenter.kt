package com.example.repacc.perfilEdicion

import android.content.Context

interface PerfilEdicionPresenter {
    fun onCreate()
    fun onDestroy()
    fun cargarDatos()
    fun asignarRH(position: Int)
    fun asignarMunicipio(position: Int)
    fun editarPerfil(context: Context)

    fun siCargoMunNotifUsuario(siCargo: Boolean)

    fun obtenerPaises(context: Context)
    fun obtenerDepartamentos(context: Context, seleccion: Int)
    fun obtenerMunicipios(context: Context, seleccion: Int)
}