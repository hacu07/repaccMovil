package com.example.repacc.vehiculo

import android.content.Context

interface VehiculoPresenter {
    fun onCreate()
    fun onDestroy()
    fun obtenerVehiculos(context: Context)
}