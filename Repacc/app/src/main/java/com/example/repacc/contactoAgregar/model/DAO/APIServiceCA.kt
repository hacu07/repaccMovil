package com.example.repacc.contactoAgregar.model.DAO

import com.example.repacc.contactoAgregar.events.ContactosEvent
import com.example.repacc.pojo.Auxiliares.SolicitudRegistrar
import com.example.repacc.util.BasicEvent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIServiceCA {
    @GET("contacto/buscarContacto/{username}")
    fun buscarContacto(@Path("username") username: String): Call<ContactosEvent>

    @POST("solicitud/registrar")
    fun registrarSolicitud(@Body solicitud: SolicitudRegistrar): Call<BasicEvent>
}