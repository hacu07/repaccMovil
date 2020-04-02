package com.example.repacc.contacto

import android.content.Context

interface ContactoPresenter {
    fun onCreate()
    fun onDestroy()
    fun cargarSolicitudes(context: Context)
    fun cargarContactos(context: Context)
    fun cambiarEstadoSolicitud(context: Context, idSolicitud: String, aceptado: Boolean)

}