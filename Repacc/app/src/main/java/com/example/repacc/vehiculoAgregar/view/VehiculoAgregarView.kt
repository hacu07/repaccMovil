package com.example.repacc.vehiculoAgregar.view

import com.example.repacc.pojo.Vehiculo

interface VehiculoAgregarView {
    fun mostrarProgreso(siMostrar:Boolean)
    fun habilitarElementos(siHabilita: Boolean)
    fun cargarSpiTipos(listaTipos: List<String>)
    fun cargarSpiMarcas(listaMarcas: List<String>)
    fun cargarSpiModelos(listaModelos: List<String>)
    fun mostrarMsj(msj: String)
    fun finalizar()
    fun agregarVehiculo(vehiculo: Vehiculo)
}