package com.example.repacc.contactoAgregar

import android.content.Context
import com.example.repacc.pojo.Usuario

interface ContactoAgregarPresenter {
    fun onCreate()
    fun onDestroy()

    fun buscarContacto(context: Context, username: String)
    fun registrarSolicitud(context: Context, usuario: Usuario)
}