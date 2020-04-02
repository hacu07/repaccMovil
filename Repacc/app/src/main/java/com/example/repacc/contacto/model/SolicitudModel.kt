package com.example.repacc.contacto.model

import android.content.Context

interface SolicitudModel {
    fun cargarSolicitud(context: Context)

    fun cargarContactos(context: Context)

    fun cambiarEstadoSolicitud(context: Context, idSolicitud: String, aceptado: Boolean)
}