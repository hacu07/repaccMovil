package com.example.repacc.menu.model.DAO

import com.example.repacc.menu.events.EstadoAgenteEvent
import com.example.repacc.menu.events.NotificacionesEvent
import com.example.repacc.pojo.Agente
import com.example.repacc.pojo.Auxiliares.SocketUsuario
import com.example.repacc.util.BasicEvent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface APIServiceAE {
    @GET("notificacion/{idUsuario}")
    fun obtenerNotificaciones(@Path("idUsuario") idUsuario: String): Call<NotificacionesEvent>

    @PUT("agente/estado")
    fun cambiarEstado(@Body agente: Agente): Call<EstadoAgenteEvent>

    @PUT("usuario/updateSocketId")
    fun updateSocketId(@Body socketUsuario: SocketUsuario): Call<SocketEvent>
}