package com.example.repacc.reportes.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.reportes.events.ReporteEvent
import com.example.repacc.reportes.events.ReportesEvent
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DAO {

    fun obtenerReporte(context: Context, idReporte: String, callback: BasicCallback){
        val service = Util.getRetrofit().create<APIServiceRS>(APIServiceRS::class.java)

        service.obtenerReporte(idReporte).enqueue(object : Callback<ReporteEvent>{
            override fun onResponse(call: Call<ReporteEvent>, response: Response<ReporteEvent>) {
                val event = response?.body()

                if (event != null){
                    event.typeEvent = if(!event.error) Util.SUCCESS else Util.ERROR_DATA
                    callback.response(event)

                }else{
                    callback.response(
                        ReporteEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }
            override fun onFailure(call: Call<ReporteEvent>, t: Throwable) {
                callback.response(
                    ReporteEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun obtenerListaReportes(context: Context, idMunicipio: String, callback: BasicCallback){
        val service = Util.getRetrofit().create<APIServiceRS>(APIServiceRS::class.java)

        service.obtenerListaReportes(idMunicipio).enqueue(object : Callback<ReportesEvent>{
            override fun onResponse(call: Call<ReportesEvent>, response: Response<ReportesEvent>) {
                val event = response?.body()

                if (event != null){
                    event.typeEvent = if(!event.error) Util.SUCCESS else Util.ERROR_DATA
                    callback.response(event)

                }else{
                    callback.response(
                        ReportesEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }
            override fun onFailure(call: Call<ReportesEvent>, t: Throwable) {
                callback.response(
                    ReportesEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun obtenerListaReportesPorCodigo(context: Context, idMunicipio: String, codigo: String, callback: BasicCallback){
        val service = Util.getRetrofit().create<APIServiceRS>(APIServiceRS::class.java)

        service.obtenerListaReportesPorCodigo(idMunicipio, codigo).enqueue(object : Callback<ReportesEvent>{
            override fun onResponse(call: Call<ReportesEvent>, response: Response<ReportesEvent>) {
                val event = response?.body()

                if (event != null){
                    event.typeEvent = if(!event.error) Util.SUCCESS else Util.ERROR_DATA
                    callback.response(event)

                }else{
                    callback.response(
                        ReportesEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }
            override fun onFailure(call: Call<ReportesEvent>, t: Throwable) {
                callback.response(
                    ReportesEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }
}