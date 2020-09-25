package com.example.repacc.vehiculoAgregar.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.pojo.Marca
import com.example.repacc.pojo.Vehiculo
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Util
import com.example.repacc.vehiculoAgregar.events.MarcaEvent
import com.example.repacc.vehiculoAgregar.events.ModeloEvent
import com.example.repacc.vehiculoAgregar.events.TipoEvent
import com.example.repacc.vehiculoAgregar.events.VehiculoEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DAO {

    val service = Util.getRetrofit().create<APIServiceVA>(APIServiceVA::class.java)

    fun obtenerTipos(context: Context, callback: BasicCallback){

        service.obtenerTipos(10).enqueue(object : Callback<TipoEvent>{
            override fun onResponse(call: Call<TipoEvent>, response: Response<TipoEvent>) {
                val tipoEvent = response?.body()

                if (tipoEvent != null){
                    tipoEvent.typeEvent = if(!tipoEvent.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(tipoEvent)
                }else{
                    callback.response(
                        TipoEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<TipoEvent>, t: Throwable) {
                callback.response(
                    TipoEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun obtenerMarcas(context: Context, idTipo: String, callback: BasicCallback){
        service.obtenerMarcas(idTipo).enqueue(object : Callback<MarcaEvent> {
            override fun onResponse(call: Call<MarcaEvent>, response: Response<MarcaEvent>) {
                val marcaEvent = response?.body()

                if (marcaEvent != null){
                    marcaEvent.typeEvent = if(!marcaEvent.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(marcaEvent)
                }else{
                    callback.response(
                        MarcaEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<MarcaEvent>, t: Throwable) {
                callback.response(
                    MarcaEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun obtenerModelos(context: Context, idMarca: String, callback: BasicCallback){
        service.obtenerModelos(idMarca).enqueue(object : Callback<ModeloEvent> {
            override fun onResponse(call: Call<ModeloEvent>, response: Response<ModeloEvent>) {
                val modeloEvent = response?.body()

                if (modeloEvent != null){
                    modeloEvent.typeEvent = if(!modeloEvent.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(modeloEvent)
                }else{
                    callback.response(
                        ModeloEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<ModeloEvent>, t: Throwable) {
                callback.response(
                    ModeloEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun registroVehiculo(context: Context, vehiculo: Vehiculo, callback: BasicCallback){
        service.registroVehiculo(vehiculo).enqueue(object: Callback<VehiculoEvent>{
            override fun onResponse(call: Call<VehiculoEvent>, response: Response<VehiculoEvent>) {
                val event = response?.body()

                if (event != null){
                    event.typeEvent = if(!event.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(event)
                }else{
                    callback.response(
                        VehiculoEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<VehiculoEvent>, t: Throwable) {
                callback.response(
                    VehiculoEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun actualizarRutaImagen(context: Context, vehiculo: Vehiculo, callback: BasicCallback) {
        service.actualizarVehiculo(vehiculo).enqueue(object: Callback<BasicEvent>{
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