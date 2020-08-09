package com.example.repacc.reporteDetalle.model.DAO

import com.example.repacc.pojo.Auxiliares.EstadoAgenRepo
import com.example.repacc.pojo.Reporte
import com.example.repacc.reporteDetalle.EstadoEvent
import com.example.repacc.util.BasicEvent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface APIServiceAR {
    @GET("estado/4")
    fun obtenerEstadosServicios(): Call<EstadoEvent>

    @PUT("reporte/estado/")
    fun actualizarEstadoReporte(@Body reporte: Reporte): Call<BasicEvent>

    @PUT("servicio/actualizar/")
    fun actualizarEstadoAgenRepo(@Body estadoAgenRepo: EstadoAgenRepo): Call<BasicEvent>
}