package com.example.repacc.registro.model.DAO


import com.example.repacc.pojo.Usuario
import com.example.repacc.registro.event.RegistroEvent
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface APIServiceRU {

    @POST("usuario/registro")
    fun registrarUsuario(@Body usuario: Usuario):Call<RegistroEvent>
}