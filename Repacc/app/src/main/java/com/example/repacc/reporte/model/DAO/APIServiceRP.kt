package com.example.repacc.reporte.model.DAO

import com.example.repacc.pojo.Reporte
import com.example.repacc.util.BasicEvent
import com.example.repacc.vehiculoAgregar.events.TipoEvent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIServiceRP {
    @GET("servicio/obtener/")
    fun obtenerServicios(): Call<TipoEvent>

    @POST("reporte/registro/")
    fun registrarReporte(@Body reporte: Reporte): Call<BasicEvent>
}