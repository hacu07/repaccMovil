package com.example.repacc.vehiculo.view

import com.example.repacc.pojo.Vehiculo

interface VehiculoView {
    fun mostrarProgreso(mostrar:Boolean)
    fun habilitarElementos(habilita:Boolean)
    fun mostrarMsj(msj: String)
    fun cargarVehiculos(listaVehiculos: ArrayList<Vehiculo>)
}