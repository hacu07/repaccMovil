package com.example.wcf.cliente.model.DAO

import android.content.Context
import com.example.wcf.R
import com.example.wcf.Util.BasicCallback
import com.example.wcf.Util.Util
import com.example.wcf.cliente.event.ClienteEvent
import com.example.wcf.pojo.Cliente
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClienteDAO {

    fun buscarCliente(context:Context, cliente: Cliente, callback: BasicCallback){
        val service = Util.getRetrofit().create<ServiceCliente>(
            ServiceCliente::class.java)

        service.buscarCliente(cliente.cedula!!).enqueue(object: Callback<ClienteEvent>{
            override fun onResponse(call: Call<ClienteEvent>, response: Response<ClienteEvent>) {
                var clienteEvent = response?.body()

                if (clienteEvent != null){
                    clienteEvent.typeEvent = if(!clienteEvent.error) Util.SUCCESS else Util.ERROR_DATA
                    clienteEvent.event =  Util.CLIENTE_EVENT_BUSQUEDA

                    callback.response(clienteEvent)
                }else{
                    callback.response(
                        ClienteEvent(
                            event =  Util.CLIENTE_EVENT_BUSQUEDA,
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ClienteEvent>, t: Throwable) {
                callback.response(
                    ClienteEvent(
                        event =  Util.CLIENTE_EVENT_BUSQUEDA,
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun guardarCliente(context:Context, cliente: Cliente, callback: BasicCallback){
        val service = Util.getRetrofit().create<ServiceCliente>(
            ServiceCliente::class.java)

        service.guardarCliente(cliente).enqueue(object: Callback<ClienteEvent>{
            override fun onResponse(call: Call<ClienteEvent>, response: Response<ClienteEvent>) {
                var clienteEvent = response?.body()

                if (clienteEvent != null){
                    clienteEvent.typeEvent = if(!clienteEvent.error) Util.SUCCESS else Util.ERROR_DATA
                    clienteEvent.event =  Util.CLIENTE_EVENT_REGISTRO
                    callback.response(clienteEvent)
                }else{
                    callback.response(
                        ClienteEvent(
                            event =  Util.CLIENTE_EVENT_REGISTRO,
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ClienteEvent>, t: Throwable) {
                callback.response(
                    ClienteEvent(
                        event =  Util.CLIENTE_EVENT_REGISTRO,
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun cambiarEstado(context:Context, cliente: Cliente, callback: BasicCallback){
        val service = Util.getRetrofit().create<ServiceCliente>(
            ServiceCliente::class.java)

        service.cambiarEstado(cliente).enqueue(object: Callback<ClienteEvent>{
            override fun onResponse(call: Call<ClienteEvent>, response: Response<ClienteEvent>) {
                var clienteEvent = response?.body()

                if (clienteEvent != null){
                    clienteEvent.typeEvent = if(!clienteEvent.error) Util.SUCCESS else Util.ERROR_DATA
                    clienteEvent.event =  Util.CLIENTE_EVENT_ESTADO
                    callback.response(clienteEvent)
                }else{
                    callback.response(
                        ClienteEvent(
                            event =  Util.CLIENTE_EVENT_ESTADO,
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ClienteEvent>, t: Throwable) {
                callback.response(
                    ClienteEvent(
                        event =  Util.CLIENTE_EVENT_ESTADO,
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }
}