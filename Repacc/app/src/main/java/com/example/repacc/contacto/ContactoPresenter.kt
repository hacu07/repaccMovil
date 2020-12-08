package com.example.repacc.contacto

import android.content.Context
import com.example.repacc.pojo.Solicitud

interface ContactoPresenter {
    fun onCreate()
    fun onDestroy()
    fun cargarSolicitudes(context: Context)
    fun cargarContactos(context: Context)
    fun cambiarEstadoSolicitud(context: Context, solicitud: Solicitud, aceptado: Boolean)

}