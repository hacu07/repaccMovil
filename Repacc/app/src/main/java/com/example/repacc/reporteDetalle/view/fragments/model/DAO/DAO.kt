package com.example.repacc.reporteDetalle.view.fragments.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.reporteDetalle.view.fragments.events.EntidadesEvent
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DAO {
    val service = Util.getRetrofit().create(APIServiceCE::class.java)

    fun buscarClinicasMunicipio(context: Context, idMunicipio: String, callback: BasicCallback){
        service.buscarClinicasMunicipio(idMunicipio).enqueue(object : Callback<EntidadesEvent>{
            override fun onResponse(
                call: Call<EntidadesEvent>,
                response: Response<EntidadesEvent>
            ) {
                val event = response?.body()
                if (event != null){
                    event.typeEvent = if (!event.error) Util.SUCCESS else Util.ERROR_DATA
                    callback.response(event)
                }else{
                    callback.response(
                        EntidadesEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<EntidadesEvent>, t: Throwable) {
                callback.response(
                    EntidadesEvent(
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }
}