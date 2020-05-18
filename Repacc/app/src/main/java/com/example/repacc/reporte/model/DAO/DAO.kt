package com.example.repacc.reporte.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.pojo.Reporte
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Util
import com.example.repacc.vehiculoAgregar.events.TipoEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DAO {
    fun obtenerServicios(context: Context, callback: BasicCallback){
        val service = Util.getRetrofit().create<APIServiceRP>(APIServiceRP::class.java)

        service.obtenerServicios().enqueue(object : Callback<TipoEvent> {
            override fun onResponse(call: Call<TipoEvent>, response: Response<TipoEvent>) {
                val servicios = response?.body()

                if(servicios != null){
                    servicios.typeEvent = if(!servicios.error) Util.SUCCESS else Util.ERROR_DATA
                    callback.response(servicios)
                }else{
                    callback.response(
                        TipoEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj= context.getString(R.string.ERROR_RESPONSE)
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

    fun registrarReporte(context: Context, reporte: Reporte, callback: BasicCallback){
        val service = Util.getRetrofit().create<APIServiceRP>(APIServiceRP::class.java)

        service.registrarReporte(reporte).enqueue(object: Callback<BasicEvent>{
            override fun onResponse(call: Call<BasicEvent>, response: Response<BasicEvent>) {
                val response = response?.body()

                if(response != null){
                    response.typeEvent = if(!response.error) Util.SUCCESS else Util.ERROR_DATA
                    callback.response(response)
                }else{
                    callback.response(
                        BasicEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj= context.getString(R.string.ERROR_RESPONSE)
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