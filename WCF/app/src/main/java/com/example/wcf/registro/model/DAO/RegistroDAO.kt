package com.example.wcf.registro.model.DAO

import android.content.Context
import com.example.wcf.R
import com.example.wcf.Util.BasicCallback
import com.example.wcf.Util.Util
import com.example.wcf.cliente.event.ClienteEvent
import com.example.wcf.pojo.Cliente
import com.example.wcf.pojo.Envios
import com.example.wcf.registro.event.ClienEvent
import com.example.wcf.registro.event.PesosEvent
import com.example.wcf.registro.event.RegistroEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroDAO {

    fun registrarEnvio(context: Context, envios: Envios, callback: BasicCallback){
        val services = Util.getRetrofit().create<ServiceRE>(ServiceRE::class.java)

        services.registrarEnvio(envios).enqueue(object : Callback<RegistroEvent>{
            override fun onResponse(call: Call<RegistroEvent>, response: Response<RegistroEvent>) {
                var registroEvent = response?.body()

                if (registroEvent != null){
                    registroEvent.typeEvent = if (!registroEvent.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(registroEvent)
                }else{
                    callback.response(
                        RegistroEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<RegistroEvent>, t: Throwable) {
                callback.response(
                    RegistroEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun buscarCliente(context: Context, cliente: Cliente, callback: BasicCallback){
        val service = Util.getRetrofit().create<ServiceRE>(ServiceRE::class.java)

        service.buscarCliente(cliente.cedula!!).enqueue(object: Callback<ClienEvent>{
            override fun onResponse(call: Call<ClienEvent>, response: Response<ClienEvent>) {
                var clienteEvent = response?.body()

                if (clienteEvent != null){
                    clienteEvent.typeEvent = if(!clienteEvent.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(clienteEvent)
                }else{
                    callback.response(
                        ClienEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ClienEvent>, t: Throwable) {
                callback.response(
                    ClienEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun obtenerPesos(context: Context,  callback: BasicCallback){
        val service = Util.getRetrofit().create<ServiceRE>(ServiceRE::class.java)

        service.obtenerPesos().enqueue(object: Callback<PesosEvent>{
            override fun onResponse(call: Call<PesosEvent>, response: Response<PesosEvent>) {
                var pesosEvent = response?.body()

                if (pesosEvent != null){
                    pesosEvent.typeEvent = if(!pesosEvent.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(pesosEvent)
                }else{
                    callback.response(
                        PesosEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<PesosEvent>, t: Throwable) {
                callback.response(
                    PesosEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

}