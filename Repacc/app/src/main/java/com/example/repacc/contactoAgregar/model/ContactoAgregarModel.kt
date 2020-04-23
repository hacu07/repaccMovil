package com.example.repacc.contactoAgregar.model

import android.content.Context
import com.example.repacc.pojo.Auxiliares.SolicitudRegistrar

interface ContactoAgregarModel {
    fun buscarContacto(context: Context, username: String)
    fun registrarSolicitud(context: Context,solicitudRegistrar: SolicitudRegistrar)
}