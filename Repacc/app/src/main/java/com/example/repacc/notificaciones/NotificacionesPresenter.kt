package com.example.repacc.notificaciones

import android.content.Context
import com.example.repacc.pojo.Reporte

interface NotificacionesPresenter {
    fun onCreate()
    fun onDestroy()
    fun obtenerReporte(context: Context, reporte: Reporte)
}