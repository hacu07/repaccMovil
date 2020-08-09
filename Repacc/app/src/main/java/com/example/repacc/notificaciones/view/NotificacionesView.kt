package com.example.repacc.notificaciones.view

import com.example.repacc.pojo.Reporte

interface NotificacionesView {
    fun habilitarElementos(habilita: Boolean)
    fun mostrarMsj(msj: String)
    fun irDetalleReporte(reporte: Reporte)
}