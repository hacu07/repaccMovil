package com.example.repacc.perfilEdicion.model.DAO

import com.example.repacc.perfilEdicion.event.DepartamentoEvent
import com.example.repacc.perfilEdicion.event.MunicipioEvent
import com.example.repacc.perfilEdicion.event.PaisEvent
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/* Servicios para Perfil Edicion */
interface APIServicePE {
    @GET("pais/paises")
    fun obtenerPaises(): Call<PaisEvent>

    @GET("departamento/{idPais}")
    fun obtenerDepartamentos(@Path("idPais") idPais: String): Call<DepartamentoEvent>

    @GET("municipio/{idDepartamento}")
    fun obtenerMunicipios(@Path("idDepartamento") idDepartamento: String): Call<MunicipioEvent>
}