package com.example.wcf.registro.model.DAO

import com.example.wcf.cliente.event.ClienteEvent
import com.example.wcf.pojo.Cliente
import com.example.wcf.pojo.Envios
import com.example.wcf.registro.event.ClienEvent
import com.example.wcf.registro.event.PesosEvent
import com.example.wcf.registro.event.RegistroEvent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServiceRE {
    @POST("envio/registro")
    fun registrarEnvio(@Body envio: Envios): Call<RegistroEvent>

    @POST("cliente/buscar")
    fun buscarCliente(@Body cliente: Cliente): Call<ClienEvent>

    @POST("pesos/ciudades")
    fun obtenerPesos(): Call<PesosEvent>
}