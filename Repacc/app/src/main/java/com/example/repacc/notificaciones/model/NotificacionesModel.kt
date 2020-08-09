package com.example.repacc.notificaciones.model

import android.content.Context
import com.example.repacc.pojo.Reporte

interface NotificacionesModel {
    fun obtenerReporte(context: Context, reporte: Reporte)
}