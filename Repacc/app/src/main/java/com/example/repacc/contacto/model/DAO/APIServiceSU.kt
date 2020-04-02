package com.example.repacc.contacto.model.DAO

import com.example.repacc.contacto.event.ContactoEvent
import com.example.repacc.contacto.event.EstadoSolicitudEvent
import com.example.repacc.contacto.event.SolicitudEvent
import com.example.repacc.pojo.Auxiliares.EstadoSolicitud
import retrofit2.Call
import retrofit2.http.*

interface APIServiceSU {
    @GET("solicitud/{iduser}")
    fun cargarSolicitudes(@Path("iduser") idUsuario: String): Call<SolicitudEvent>

    @GET("contacto/buscar/{iduser}")
    fun cargarContactos(@Path("iduser") idUsuario: String): Call<ContactoEvent>

    @PUT("solicitud/cambiarEstado")
    fun cambiarEstadoSolicitud(@Body estadoSolicitud: EstadoSolicitud): Call<EstadoSolicitudEvent>
}