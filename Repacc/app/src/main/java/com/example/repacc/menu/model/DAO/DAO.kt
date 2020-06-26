package com.example.repacc.menu.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.menu.events.EstadoAgenteEvent
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DAO {

    /***********************************************************************
     * Realiza actualizacion de estado del agente en el servidor
     * HAROLDC 23/06/2020
     */
    fun cambiarEstado(context: Context, disponible: Boolean, callback: BasicCallback){
        val service = Util.getRetrofit().create<APIServiceAE>(
            APIServiceAE::class.java)

        Constantes.config?.agente!!.disponible = disponible

        service.cambiarEstado(Constantes.config?.agente!!).enqueue(object: Callback<EstadoAgenteEvent>{
            override fun onResponse(call: Call<EstadoAgenteEvent>, response: Response<EstadoAgenteEvent>) {
                val response = response?.body()

                if (response != null){
                    response.typeEvent = if(!response.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(
                        response
                    )
                }else{
                    callback.response(
                        BasicEvent(
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<EstadoAgenteEvent>, t: Throwable) {
                callback.response(
                    BasicEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }
}