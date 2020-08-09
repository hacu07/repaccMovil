package com.example.repacc.reporteDetalle

import android.content.Context
import com.example.repacc.pojo.Entidad
import com.example.repacc.pojo.Reporte
import com.example.repacc.pojo.Servicio

interface ReporteDetallePresenter {
    fun onCreate()
    fun onDestroy()
    fun actualizarEstadoReporte(context: Context, reporte: Reporte)
    fun obtenerEstadosServicios(context: Context)
    fun actualizarEstadoAgenRepo(
        context: Context,
        position: Int,
        servicio: Servicio,
        entidad: Entidad?,
        descriptraslado: String?
    )
}