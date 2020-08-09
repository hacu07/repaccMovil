package com.example.repacc.reporteDetalle.view.fragments.model.DAO

import com.example.repacc.reporteDetalle.view.fragments.events.EntidadesEvent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface APIServiceCE {
    @GET("entidad/{idMunicipio}")
    fun buscarClinicasMunicipio(@Path("idMunicipio") idMunicipio: String) : Call<EntidadesEvent>
}