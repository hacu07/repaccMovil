package com.example.wcf.cliente.model.DAO

import com.example.wcf.cliente.event.ClienteEvent
import com.example.wcf.pojo.Cliente
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceCliente {
    @POST("cliente/buscar")
    fun buscarCliente(@Body cliente: Cliente): Call<ClienteEvent>

    @POST("Service.svc/crearCliente")
    fun guardarCliente(@Body cliente: Cliente): Call<ClienteEvent>

    @POST("cliente/cambiarestado")
    fun cambiarEstado(@Body cliente: Cliente): Call<ClienteEvent>
}