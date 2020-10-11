package com.example.repacc.contactoAgregar.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.contactoAgregar.events.ContactosEvent
import com.example.repacc.pojo.Auxiliares.SolicitudRegistrar
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactoAgregarDAO {

    fun buscarUsuario(context: Context, username: String, callback: BasicCallback){
        val service = Util.getRetrofit().create<APIServiceCA>(APIServiceCA::class.java)

        service.buscarContacto(Constantes.config!!.usuario!!._id!! ,username).enqueue(object: Callback<ContactosEvent>{
            override fun onResponse(
                call: Call<ContactosEvent>,
                response: Response<ContactosEvent>
            ) {
                val event = response?.body()
                if (event != null){
                    event.typeEvent = if(!event.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(event)
                }else{
                    callback.response(
                        ContactosEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ContactosEvent>, t: Throwable) {
                callback.response(
                    ContactosEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun registrarSolicitud(context: Context, registrar: SolicitudRegistrar, callback: BasicCallback){
        val service = Util.getRetrofit().create<APIServiceCA>(APIServiceCA::class.java)

        service.registrarSolicitud(registrar).enqueue(object: Callback<BasicEvent>{
            override fun onResponse(call: Call<BasicEvent>, response: Response<BasicEvent>) {
                val event = response?.body()
                if (event != null){
                    event.typeEvent = if(!event.error) Util.SUCCESS else Util.ERROR_DATA
                    callback.response(event)
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