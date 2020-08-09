package com.example.repacc.reporteDetalle.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.pojo.Auxiliares.EstadoAgenRepo
import com.example.repacc.pojo.Reporte
import com.example.repacc.reporteDetalle.EstadoEvent
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DAO {
    private val service = Util.getRetrofit().create<APIServiceAR>(APIServiceAR::class.java)

    fun actualizarEstadoReporte(context: Context, reporte: Reporte, callback: BasicCallback){
        service.actualizarEstadoReporte(reporte).enqueue(object :Callback<BasicEvent>{
            override fun onResponse(call: Call<BasicEvent>, response: Response<BasicEvent>) {
                val event = response?.body()

                if (event != null){
                    event.typeEvent = if(!event.error) Util.SUCCESS else Util.ERROR_DATA
                    callback.response(event)
                }else
                    callback.response(
                        BasicEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_CONEXION)
                        )
                    )
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

    fun obtenerEstadosServicios(context: Context, callback: BasicCallback){
        service.obtenerEstadosServicios().enqueue(object: Callback<EstadoEvent>{
            override fun onResponse(call: Call<EstadoEvent>, response: Response<EstadoEvent>) {
                val event = response?.body()

                if (event != null){
                    event.typeEvent = if(!event.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(event)
                }else{
                    callback.response(
                        EstadoEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<EstadoEvent>, t: Throwable) {
                callback.response(
                    EstadoEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun actualizarEstadoAgenRepo(context: Context, estadoAgenRepo: EstadoAgenRepo, callback: BasicCallback){
        service.actualizarEstadoAgenRepo(estadoAgenRepo).enqueue(object : Callback<BasicEvent>{
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