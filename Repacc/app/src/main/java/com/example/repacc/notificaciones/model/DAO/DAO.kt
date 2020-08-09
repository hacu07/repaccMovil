package com.example.repacc.notificaciones.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.pojo.Reporte
import com.example.repacc.reportes.events.ReporteEvent
import com.example.repacc.reportes.model.DAO.APIServiceRS
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DAO {
    private val service = Util.getRetrofit().create<APIServiceRS>(APIServiceRS::class.java)

    fun obtenerReporte(context: Context, reporte: Reporte, callback: BasicCallback){

        service.obtenerReporte(reporte._id!!).enqueue(object : Callback<ReporteEvent> {
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
}