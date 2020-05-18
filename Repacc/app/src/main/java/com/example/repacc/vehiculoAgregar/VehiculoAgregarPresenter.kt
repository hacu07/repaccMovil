package com.example.repacc.vehiculoAgregar

import android.content.Context

interface VehiculoAgregarPresenter {
    fun onCreate()
    fun onDestroy()
    fun obtenerTipos(context: Context)
    fun obtenerMarcas(context: Context, position: Int)
    fun obtenerModelos(context: Context, position: Int)
    fun asignarModelo(position: Int)
    fun registrarVehiculo(context: Context, placa: String, esParticular: Boolean, colores: Array<String>)
}