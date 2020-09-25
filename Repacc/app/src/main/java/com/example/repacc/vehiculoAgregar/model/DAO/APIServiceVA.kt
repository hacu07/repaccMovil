package com.example.repacc.vehiculoAgregar.model.DAO

import com.example.repacc.pojo.Vehiculo
import com.example.repacc.util.BasicEvent
import com.example.repacc.vehiculoAgregar.events.MarcaEvent
import com.example.repacc.vehiculoAgregar.events.ModeloEvent
import com.example.repacc.vehiculoAgregar.events.TipoEvent
import com.example.repacc.vehiculoAgregar.events.VehiculoEvent
import retrofit2.Call
import retrofit2.http.*

interface APIServiceVA {
    @GET("tipo/buscar/{tipo}")
    fun obtenerTipos(@Path("tipo") tipo: Int): Call<TipoEvent>

    @GET("marca/buscar/{tipo}")
    fun obtenerMarcas(@Path("tipo") tipo: String): Call<MarcaEvent>

    @GET("modelo/buscar/{idMarca}")
    fun obtenerModelos(@Path("idMarca") idMarca: String): Call<ModeloEvent>

    @POST("vehiculo/registro/")
    fun registroVehiculo(@Body vehiculo: Vehiculo): Call<VehiculoEvent>

    @PUT("vehiculo/update/")
    fun actualizarVehiculo(@Body vehiculo: Vehiculo): Call<BasicEvent>
}