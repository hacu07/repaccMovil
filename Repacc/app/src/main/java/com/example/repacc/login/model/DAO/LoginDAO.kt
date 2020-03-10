package com.example.repacc.login.model.DAO

import android.content.Context
import com.example.repacc.R
import com.example.repacc.login.event.LoginEvent
import com.example.repacc.pojo.Config
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginDAO {

    fun validarSesion(context: Context, usuario: Usuario, callbackLogin: CallbackLogin){
        usuario.let {
            val service = Util.getRetrofit().create<APIServiceIS>(
                APIServiceIS::class.java)

            service.validarUsuario(usuario).enqueue(object : Callback<LoginEvent>{
                override fun onResponse(call: Call<LoginEvent>, response: Response<LoginEvent>) {
                    val loginEvent = response?.body()

                    loginEvent?.let{

                        // Retorno error
                        if (it.error){
                            it.typeEvent = Util.ERROR_RESPONSE
                            callbackLogin.onError(it)
                            return
                        }

                        // Si no ocurrio error, continua...

                        // Obtiene JWT de header enviado
                        val listHeaders = response.headers()
                        var jwt:String? = null
                        // Si obtuvo la lista de headers
                        if (listHeaders != null){
                            // Asigna el valor del jwt si lo ha enviado por header
                            jwt = listHeaders.get(Constantes.HEADER_AUTHORIZATION)
                        }

                        if (jwt != null){
                            Constantes.config = Config(it.content, jwt)
                            // Guarda en preferencias los datos del usuario y jwt
                            Util.guardarConfig(context)
                            callbackLogin.onSuccess(it)
                        }
                        else{
                            // No obtuvo JWT, se lanza error
                            it.typeEvent = Util.ERROR_DATA
                            callbackLogin.onError(it)
                        }

                        return
                    }

                    // Si continua no encontro respuesta
                    callbackLogin.onError(
                        LoginEvent(
                            typeEvent = Util.ERROR_RESPONSE,
                            msj = context.getString(R.string.ERROR_RESPONSE)
                        )
                    )
                }

                override fun onFailure(call: Call<LoginEvent>, t: Throwable) {
                    callbackLogin.onError(
                        LoginEvent(
                            typeEvent = Util.ERROR_CONEXION,
                            msj = context.getString(R.string.ERROR_CONEXION)
                        )
                    )
                }
            })
        }
    }
}