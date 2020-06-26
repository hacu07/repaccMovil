package com.example.repacc.menu.model.DAO

import com.example.repacc.menu.events.EstadoAgenteEvent
import com.example.repacc.pojo.Agente
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT

interface APIServiceAE {
    @PUT("agente/estado")
    fun cambiarEstado(@Body agente: Agente): Call<EstadoAgenteEvent>
}