package com.example.repacc.menu.view

import com.example.repacc.pojo.Auxiliares.SocketUsuario
import com.example.repacc.pojo.Notificacion

interface MenuView {
    fun habilitarElementos(habilita: Boolean)
    fun mostrarMsj(msj: String)
    fun asignarEstado(estadoAnt: Boolean)
    fun mostrarNotificaciones(notificaciones: ArrayList<Notificacion>)
    fun setEmitterListener(
        socketUsuario: SocketUsuario?,
        lastSocketId: String?
    )
}