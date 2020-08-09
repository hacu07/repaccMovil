package com.example.repacc.reporteDetalle.view

import com.example.repacc.pojo.Estado
import com.example.repacc.pojo.Reporte

interface ReporteDetalleView {
    fun mostrarMsj(msj: String)
    fun validarFalsaAlarma()
    fun setReporte(reporte: Reporte)
    fun reloadServices(
        listaEstados: MutableList<String>,
        estados: ArrayList<Estado>
    )
}