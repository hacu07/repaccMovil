package com.example.repacc.registro.model.DAO

import com.example.repacc.pojo.Usuario
import com.example.repacc.registro.event.RegistroEvent
import com.example.repacc.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistroDAO {

    fun registrarUsuario(usuario: Usuario, callbackRegistro: CallbackRegistro){
        usuario?.let {
            val service = Util.getRetrofit()?.create<APIServiceRU>(APIServiceRU::class.java)

            service.registrarUsuario(usuario).enqueue(object: Callback<RegistroEvent>{
                override fun onResponse(
                    call: Call<RegistroEvent>,
                    response: Response<RegistroEvent>
                ) {
                    val registroEvent = response?.body()

                    registroEvent?.let {
                        it.typeEvent =  if(it.error) Util.ERROR_RESPONSE else Util.SUCCESS
                    }

                    if (registroEvent != null)
                        callbackRegistro.onSuccess(registroEvent)
                    else
                        callbackRegistro.onError(RegistroEvent(typeEvent = Util.ERROR_RESPONSE, msj = "Error de respuesta."))
                }

                override fun onFailure(call: Call<RegistroEvent>, t: Throwable) {
                    callbackRegistro.onError(
                        RegistroEvent(typeEvent = Util.ERROR_CONEXION, msj = "Error de conexión")
                    )
                }

            })
        }
    }
}