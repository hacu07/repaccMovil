package com.example.wcf.envios

import android.content.Context

interface EnvioPresenter {
    fun onCreate()
    fun onDestroy()

    fun consultarEnvio(context: Context, idGuia: Int)
    fun cambiarEstado(context: Context, idGuia:Int, cod_estado: Int)
}