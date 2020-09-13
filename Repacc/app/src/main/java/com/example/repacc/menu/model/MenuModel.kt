package com.example.repacc.menu.model

import android.content.Context
import com.example.repacc.pojo.Auxiliares.SocketUsuario

interface MenuModel {
    fun cambiarEstado(context: Context, disponible: Boolean)
    fun obtenerNotificaciones(context: Context)
    fun initSocket(socketUsuario: SocketUsuario)
}