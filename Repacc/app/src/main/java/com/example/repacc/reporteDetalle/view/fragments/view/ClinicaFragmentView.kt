package com.example.repacc.reporteDetalle.view.fragments.view

import com.example.repacc.pojo.Entidad

interface ClinicaFragmentView {
    fun habilitarElementos(habilita: Boolean)
    fun mostrarProgreso(muestra: Boolean)


    fun mostrarMsj(msj: String)
    fun ocultarSpinnerEntidades()
    fun cargarEntidades(entidades: ArrayList<Entidad>?, nombresEntidades: MutableList<String>)
}