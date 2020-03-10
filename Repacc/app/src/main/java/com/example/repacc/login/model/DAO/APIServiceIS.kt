package com.example.repacc.login.model.DAO

import com.example.repacc.login.event.LoginEvent
import com.example.repacc.pojo.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// Inicio Sesion
interface APIServiceIS {
    @POST("usuario/login")
    fun validarUsuario(@Body usuario: Usuario): Call<LoginEvent>
}