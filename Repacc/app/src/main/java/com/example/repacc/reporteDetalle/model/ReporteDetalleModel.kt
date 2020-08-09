package com.example.repacc.reporteDetalle.model

import android.content.Context
import com.example.repacc.pojo.Auxiliares.EstadoAgenRepo
import com.example.repacc.pojo.Reporte

interface ReporteDetalleModel {
    fun actualizarEstadoReporte(context: Context, reporte: Reporte)
    fun actualizarEstadoAgenRepo(context: Context, estadoAgenRepo: EstadoAgenRepo)
    fun obtenerEstadosServicios(context: Context)
}