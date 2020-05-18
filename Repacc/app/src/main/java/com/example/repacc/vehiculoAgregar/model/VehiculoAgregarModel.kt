package com.example.repacc.vehiculoAgregar.model

import android.content.Context
import com.example.repacc.pojo.Vehiculo

interface VehiculoAgregarModel {
    fun obtenerTipos(context: Context)
    fun obtenerMarcas(context: Context, idTipo: String)
    fun obtenerModelos(context: Context, idMarca: String)
    fun registroVehiculo(context: Context, vehiculo: Vehiculo)
}