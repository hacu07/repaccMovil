package com.example.repacc.contacto.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.contacto.event.ContactoEvent
import com.example.repacc.contacto.event.EstadoSolicitudEvent
import com.example.repacc.contacto.event.SolicitudEvent
import com.example.repacc.pojo.Auxiliares.EstadoSolicitud
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactoDAO {
    fun cargarSolicitudes(context: Context, callback: SolicitudCallback){
        val service = Util.getRetrofit().create<APIServiceSU>(APIServiceSU::class.java)

        if (Constantes.config?.usuario?._id != null){

            var idUser = ""

            Constantes.config?.usuario?._id.let {
                if (it != null) {
                    idUser = it
                }
            }

            service.cargarSolicitudes(idUser).enqueue(object: Callback<SolicitudEvent>{

                override fun onResponse(
                    call: Call<SolicitudEvent>,
                    response: Response<SolicitudEvent>
                ) {
                    val solicitudEvent = response?.body()

                    if (solicitudEvent != null){
                        solicitudEvent.typeEvent = if(!solicitudEvent.error) Util.SUCCESS else Util.ERROR_DATA
                        callback.response(solicitudEvent)
                    }else{
                        callback.response(
                            SolicitudEvent(
                                typeEvent = Util.ERROR_RESPONSE,
                                msj = context.getString(R.string.ERROR_CONEXION)
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<SolicitudEvent>, t: Throwable) {
                    callback.response(
                        SolicitudEvent(
                            msj = context.getString(R.string.ERROR_CONEXION)
                        )
                    )
                }

            })
        }
    }


    /*
    * Obtiene los contactos registrados al usuario de la sesion
    * */
    fun cargarContactos(context: Context, callback: ContactoCallback){
        val service = Util.getRetrofit().create<APIServiceSU>(APIServiceSU::class.java)

        if (Constantes.config?.usuario?._id != null){
            var idUser = "" //Id del usuario que inicio sesion
            Constantes.config?.usuario?._id.let {
                if (it != null) {
                    idUser = it
                }
            }
            //Envia ID de usuario que inicio sesion para buscar contactos
            service.cargarContactos(idUser).enqueue(object: Callback<ContactoEvent>{
                override fun onResponse(
                    call: Call<ContactoEvent>,
                    response: Response<ContactoEvent>
                ) {
                    val contactoEvent = response?.body()

                    if (contactoEvent != null){
                        contactoEvent.typeEvent = if(!contactoEvent.error) Util.SUCCESS else Util.ERROR_DATA
                        callback.response(contactoEvent)
                    }else{
                        callback.response(
                            ContactoEvent(
                                typeEvent = Util.ERROR_RESPONSE,
                                msj = context.getString(R.string.ERROR_RESPONSE)
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<ContactoEvent>, t: Throwable) {
                    callback.response(
                        ContactoEvent(
                            msj = context.getString(R.string.ERROR_CONEXION)
                        )
                    )
                }
            })
        }
    }

    /*
    * Cambia el estado de la solicitud segun ha sido aceptada o rechazada
    * */
    fun cambiarEstado(context: Context, idSolicitud: String, aceptado: Boolean, callback: BasicCallback){
        val service = Util.getRetrofit().create<APIServiceSU>(APIServiceSU::class.java)

        service.cambiarEstadoSolicitud(EstadoSolicitud(idSolicitud, aceptado)).enqueue(object: Callback<EstadoSolicitudEvent>{
            override fun onResponse(
                call: Call<EstadoSolicitudEvent>,
                response: Response<EstadoSolicitudEvent>
            ) {
                val estadoSolicitudEvent = response?.body()

                if (estadoSolicitudEvent != null){
                    estadoSolicitudEvent.typeEvent = if(!estadoSolicitudEvent.error) Util.SUCCESS else Util.ERROR_DATA
                    callback.response(estadoSolicitudEvent)
                }else{
                    callback.response(
                        EstadoSolicitudEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<EstadoSolicitudEvent>, t: Throwable) {
                callback.response(
                    SolicitudEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }
}