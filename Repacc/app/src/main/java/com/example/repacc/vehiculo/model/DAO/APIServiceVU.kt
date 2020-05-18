package com.example.repacc.vehiculo.model.DAO

import com.example.repacc.vehiculo.events.VehiculoEvent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface APIServiceVU {
    @GET("vehiculo/buscar/{idUsuario}")
    fun obtenerVehiculos(@Path("idUsuario") idUsuario: String): Call<VehiculoEvent>
}