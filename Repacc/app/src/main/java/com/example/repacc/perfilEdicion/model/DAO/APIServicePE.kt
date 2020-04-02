package com.example.repacc.perfilEdicion.model.DAO

import com.example.repacc.perfilEdicion.event.DepartamentoEvent
import com.example.repacc.perfilEdicion.event.EdicionPerfilEvent
import com.example.repacc.perfilEdicion.event.MunicipioEvent
import com.example.repacc.perfilEdicion.event.PaisEvent
import com.example.repacc.pojo.Usuario
import retrofit2.Call
import retrofit2.http.*

/* Servicios para Perfil Edicion */
interface APIServicePE {
    @GET("pais/paises")
    fun obtenerPaises(): Call<PaisEvent>

    @GET("departamento/{idPais}")
    fun obtenerDepartamentos(@Path("idPais") idPais: String): Call<DepartamentoEvent>

    @GET("municipio/{idDepartamento}")
    fun obtenerMunicipios(@Path("idDepartamento") idDepartamento: String): Call<MunicipioEvent>

    @PUT("usuario/edicion")
    fun editarPerfil(@Body usuario: Usuario): Call<EdicionPerfilEvent>
}