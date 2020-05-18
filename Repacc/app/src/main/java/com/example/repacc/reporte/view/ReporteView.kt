package com.example.repacc.reporte.view

import com.example.repacc.pojo.Tipo

interface ReporteView {
    fun habilitarElementos(habilita: Boolean)
    fun mostrarMsj(msj: String)
    fun mostrarProgreso(mostrar: Boolean)
    fun cargarServicios(listaServicios: ArrayList<Tipo>)
    fun mostrarDialogo(msj: String)
}