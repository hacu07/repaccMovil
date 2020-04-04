package com.example.wcf.registro.model.DAO

import com.example.wcf.cliente.event.ClienteEvent
import com.example.wcf.pojo.Cliente
import com.example.wcf.pojo.Envios
import com.example.wcf.registro.event.ClienEvent
import com.example.wcf.registro.event.PesosEvent
import com.example.wcf.registro.event.RegistroEvent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServiceRE {
    @POST("/registrarEnvio")
    fun registrarEnvio(@Body envio: Envios): Call<RegistroEvent>

    @GET("/getCliente/{id}")
    fun buscarCliente(@Path("id") id: String): Call<ClienEvent>

    @GET("/getPesos")
    fun obtenerPesos(): Call<PesosEvent>
}