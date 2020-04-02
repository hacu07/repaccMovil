package com.example.wcf.Util

import android.content.Context
import android.widget.Toast
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Util {

    companion object{
        lateinit var URL_API: String

        val SUCCESS = 0
        val ERROR_DATA = 100
        val ERROR_RESPONSE = 101
        val ERROR_CONEXION = 102

        /*
        * Evento segun cliente
        * */
        val CLIENTE_EVENT_REGISTRO = 100
        val CLIENTE_EVENT_BUSQUEDA = 101
        val CLIENTE_EVENT_ESTADO = 102

        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun mostrarMsj(context: Context, msj: String){
            Toast.makeText(context,msj,Toast.LENGTH_LONG).show()
        }
    }
}