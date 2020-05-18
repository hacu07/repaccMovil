package com.example.repacc.reportes.model.DAO

import com.example.repacc.reportes.events.ReporteEvent
import com.example.repacc.reportes.events.ReportesEvent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface APIServiceRS {
    @GET("reporte/valido/{idReporte}")
    fun obtenerReporte(@Path("idReporte") idReporte: String): Call<ReporteEvent>

    @GET("reporte/basicInfo/{idCiudad}")
    fun obtenerListaReportes(@Path("idCiudad") idCiudad: String): Call<ReportesEvent>
}