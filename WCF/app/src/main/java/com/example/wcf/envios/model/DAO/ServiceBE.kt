package com.example.wcf.envios.model.DAO

import com.example.wcf.Util.BasicEvent
import com.example.wcf.envios.events.EnvioEvent
import com.example.wcf.envios.events.EstadoEvent
import com.example.wcf.pojo.EnvioEstado
import com.example.wcf.pojo.Envios
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

/* Servicio de busqueda de envio */
interface ServiceBE {
    @POST("envio/registro")
    fun cambiarEstadoEnvio(@Body envio: EnvioEstado): Call<BasicEvent>

    @POST("envio/{idGuia}")
    fun consultarEnvio(@Path("idGuia") idGuia: Int): Call<EnvioEvent>
}