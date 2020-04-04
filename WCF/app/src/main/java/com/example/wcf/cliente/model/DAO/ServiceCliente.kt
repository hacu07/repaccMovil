package com.example.wcf.cliente.model.DAO

import com.example.wcf.cliente.event.ClienteEvent
import com.example.wcf.pojo.Cliente
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServiceCliente {
    @GET("/getCliente/{id}")
    fun buscarCliente(@Path("id") id: String): Call<ClienteEvent>

    @POST("/crearCliente")
    fun guardarCliente(@Body cliente: Cliente): Call<ClienteEvent>

    @POST("/actualizarEstadoCliente")
    fun cambiarEstado(@Body cliente: Cliente): Call<ClienteEvent>
}