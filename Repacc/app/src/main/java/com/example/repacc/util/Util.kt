package com.example.repacc.util

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Util {

    //Elementos estaticos
    companion object {

        lateinit var URL_API: String

        val SUCCESS = 0
        val ERROR_DATA = 100
        val ERROR_RESPONSE = 101
        val ERROR_CONEXION = 102

        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun mostrarToast(context: Context, msj: String){
            Toast.makeText(context,msj,Toast.LENGTH_LONG).show()
        }

        /*
        * Almacena la configuracion del usuario en las preferencias
        * */
        fun guardarConfig(context: Context):Boolean {
            var l_guardo = false;

            // Obtiene la preferencia de configuracion
            var preferences = context.getSharedPreferences(
                    Constantes.TAG_CONFIG,
                    Constantes.PREFERENCE_PRIVATE_MODE
            )

            if (preferences != null &&  context != null && Constantes.config != null){
                //Permite editar el archivo de las preferencias
                val editor: SharedPreferences.Editor = preferences.edit()

                //agrega la configuracion en formato JSON a las preferencias
                editor.putString(Constantes.PREFERENCES_CONFIG, Gson().toJson(Constantes.config))

                // Guarda en preferencias
                l_guardo = editor.commit()
            }

            return l_guardo
        }
    }


}