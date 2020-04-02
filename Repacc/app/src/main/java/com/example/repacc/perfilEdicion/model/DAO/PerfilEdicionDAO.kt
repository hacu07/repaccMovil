package com.example.repacc.perfilEdicion.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.perfilEdicion.event.DepartamentoEvent
import com.example.repacc.perfilEdicion.event.EdicionPerfilEvent
import com.example.repacc.perfilEdicion.event.MunicipioEvent
import com.example.repacc.perfilEdicion.event.PaisEvent
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilEdicionDAO {

    fun obtenerPaises(context: Context, paisCallback: PaisCallback){
        val service = Util.getRetrofit().create<APIServicePE>(APIServicePE::class.java)

        service.obtenerPaises().enqueue(object :Callback<PaisEvent>{
            override fun onResponse(call: Call<PaisEvent>, response: Response<PaisEvent>) {
                val paisEvent = response?.body()

                if (paisEvent != null){
                    // Si no hubo error = SUCCESS
                    paisEvent.typeEvent = if(!paisEvent.error) Util.SUCCESS else Util.ERROR_DATA
                    paisCallback.response(paisEvent)
                }else{
                    // No se logro obtener respuesta
                    paisCallback.response(
                        PaisEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<PaisEvent>, t: Throwable) {
                paisCallback.response(
                    PaisEvent(
                        typeEvent = Util.ERROR_CONEXION,
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }


    fun obtenerDepartamentos(context: Context, idPais: String,  dptoCallback: DepartamentoCallback){
        val service = Util.getRetrofit().create<APIServicePE>(APIServicePE::class.java)

        service.obtenerDepartamentos(idPais).enqueue(object :Callback<DepartamentoEvent>{
            override fun onResponse(call: Call<DepartamentoEvent>, response: Response<DepartamentoEvent>) {
                val dptoEvent = response?.body()

                if (dptoEvent != null){
                    // Si no hubo error = SUCCESS
                    dptoEvent.typeEvent = if(!dptoEvent.error) Util.SUCCESS else Util.ERROR_DATA
                    dptoCallback.response(dptoEvent)
                }else{
                    // No se logro obtener respuesta
                    dptoCallback.response(
                        DepartamentoEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<DepartamentoEvent>, t: Throwable) {
                dptoCallback.response(
                    DepartamentoEvent(
                        typeEvent = Util.ERROR_CONEXION,
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }


    fun obtenerMunicipios(context: Context, idDepto: String,  mnpioCallback: MunicipioCallback){
        val service = Util.getRetrofit().create<APIServicePE>(APIServicePE::class.java)

        service.obtenerMunicipios(idDepto).enqueue(object :Callback<MunicipioEvent>{
            override fun onResponse(call: Call<MunicipioEvent>, response: Response<MunicipioEvent>) {
                val municipioEvent = response?.body()

                if (municipioEvent != null){
                    // Si no hubo error = SUCCESS
                    municipioEvent.typeEvent = if(!municipioEvent.error) Util.SUCCESS else Util.ERROR_DATA
                    mnpioCallback.response(municipioEvent)
                }else{
                    // No se logro obtener respuesta
                    mnpioCallback.response(
                        MunicipioEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }
            }

            override fun onFailure(call: Call<MunicipioEvent>, t: Throwable) {
                mnpioCallback.response(
                    MunicipioEvent(
                        typeEvent = Util.ERROR_CONEXION,
                        msj = context.getString(R.string.ERROR_CONEXION)
                    )
                )
            }
        })
    }

    fun editarPerfil(context: Context, usuario: Usuario, callback: EdicionPerfilCallback){
        val service = Util.getRetrofit().create<APIServicePE>(APIServicePE::class.java)

        service.editarPerfil(usuario).enqueue(object: Callback<EdicionPerfilEvent>{
            override fun onResponse(call: Call<EdicionPerfilEvent>,response: Response<EdicionPerfilEvent>) {
                val event = response?.body()

                if(event != null){
                    event.typeEvent = if(!event.error) Util.SUCCESS else Util.ERROR_DATA
                    callback.response(event)
                }else{
                    callback.response(
                        EdicionPerfilEvent(
                            typeEvent =  Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        ))
                }
            }
            override fun onFailure(call: Call<EdicionPerfilEvent>, t: Throwable) {
                callback.response(
                    EdicionPerfilEvent(
                    typeEvent =  Util.ERROR_CONEXION,
                    msj = context.getString(R.string.ERROR_CONEXION)
                ))
            }
        })
    }
}