package com.example.repacc.vehiculo.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import com.example.repacc.vehiculo.events.VehiculoEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DAO {
    fun obtenerVehiculos(context: Context, callback: BasicCallback){
        val service = Util.getRetrofit().create<APIServiceVU>(
            APIServiceVU::class.java)

        service.obtenerVehiculos(Constantes.config!!.usuario!!._id!!).enqueue(object : Callback<VehiculoEvent> {
            override fun onResponse(call: Call<VehiculoEvent>, response: Response<VehiculoEvent>) {
                val vehiculoEvent = response?.body()

                if (vehiculoEvent != null){
                    vehiculoEvent.typeEvent = if(!vehiculoEvent.error) Util.SUCCESS else Util.ERROR_DATA

                    callback.response(vehiculoEvent)
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
}