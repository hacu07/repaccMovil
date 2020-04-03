package com.example.wcf.envios.model.DAO

import android.content.Context
import com.example.wcf.R
import com.example.wcf.Util.BasicCallback
import com.example.wcf.Util.BasicEvent
import com.example.wcf.Util.Util
import com.example.wcf.envios.events.EnvioEvent
import com.example.wcf.pojo.EnvioEstado
import com.example.wcf.pojo.Envios
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EnvioDAO {

    fun consultarEnvio(context: Context, idGuia: Int, callback: BasicCallback){
        val service = Util.getRetrofit().create<ServiceBE>(
            ServiceBE::class.java)

        service.consultarEnvio(idGuia).enqueue(object: Callback<EnvioEvent>{
            override fun onResponse(call: Call<EnvioEvent>, response: Response<EnvioEvent>) {
                val envioEvent = response?.body()

                if (envioEvent != null){
                    envioEvent.typeEvent = if (!envioEvent.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(envioEvent)
                }else{
                    callback.response(
                        EnvioEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<EnvioEvent>, t: Throwable) {
                callback.response(
                    EnvioEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun cambiarEstadoEnvio(context: Context, envio: EnvioEstado, callback: BasicCallback){
        val service = Util.getRetrofit().create<ServiceBE>(
            ServiceBE::class.java)

        service.cambiarEstadoEnvio(envio).enqueue(object: Callback<BasicEvent>{
            override fun onResponse(call: Call<BasicEvent>, response: Response<BasicEvent>) {
                val basicEvent = response?.body()

                if (basicEvent != null){
                    basicEvent.typeEvent = if (!basicEvent.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(
                        basicEvent
                    )
                }else{
                    callback.response(
                        BasicEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )

                }
            }

            override fun onFailure(call: Call<BasicEvent>, t: Throwable) {
                callback.response(
                    BasicEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }
}