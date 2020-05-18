package com.example.repacc.reportes.view

import com.example.repacc.pojo.Reporte
import java.io.Serializable

interface ReportesView : Serializable{
    fun mostrarProgreso(mostrar: Boolean)
    fun habilitarElementos(habilita: Boolean)
    fun mostrarMsj(msj: String)
    fun cargarReportes(listaReportes: ArrayList<Reporte>)
    fun mostrarReporte(reporte: Reporte)
}