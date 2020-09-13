package com.example.repacc.menu.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.menu.events.EstadoAgenteEvent
import com.example.repacc.menu.events.NotificacionesEvent
import com.example.repacc.pojo.Auxiliares.SocketUsuario
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DAO {

    fun obtenerNotificaciones(context: Context, callback: BasicCallback){
        val service = Util.getRetrofit().create(APIServiceAE::class.java)

        service.obtenerNotificaciones(Constantes.config?.usuario?._id!!).enqueue(object : Callback<NotificacionesEvent> {
            override fun onResponse(
                call: Call<NotificacionesEvent>,
                response: Response<NotificacionesEvent>
            ) {
                val event = response?.body()

                if (event != null){
                    event.typeEvent = if(!event.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(event)
                }else{
                    callback.response(
                        NotificacionesEvent(
                            msj = context.getString(R.string.ERROR_RESPONSE),
                            typeEvent = Util.ERROR_RESPONSE
                        )
                    )
                }
            }

            override fun onFailure(call: Call<NotificacionesEvent>, t: Throwable) {
                callback.response(
                    NotificacionesEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

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
                        EstadoAgenteEvent(
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<EstadoAgenteEvent>, t: Throwable) {
                callback.response(
                    EstadoAgenteEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    /************************************
     * Actualiza el socketId en BD
     * HAROLDC 23/08/2020
     */
    fun updateSocketId(
        socketUsuario: SocketUsuario,
        basicCallback: BasicCallback
    ){
        val service = Util.getRetrofit().create<APIServiceAE>(
            APIServiceAE::class.java)

        service.updateSocketId(socketUsuario).enqueue(object: Callback<SocketEvent>{
            override fun onResponse(call: Call<SocketEvent>, response: Response<SocketEvent>) {
                val event = response?.body()

                if (event != null){
                    event.typeEvent = if(!event.error) Util.SUCCESS else Util.ERROR_DATA
                    event.socketUsuario = socketUsuario
                    basicCallback.response(event)
                }else{
                    basicCallback.response(
                        SocketEvent(
                            typeEvent = Util.ERROR_RESPONSE
                        )
                    )
                }
            }

            override fun onFailure(call: Call<SocketEvent>, t: Throwable) {
                basicCallback.response(
                    SocketEvent()
                )
            }
        })
    }
}