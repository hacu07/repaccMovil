package com.example.wcf.envios.model

import android.content.Context
import com.example.wcf.pojo.EnvioEstado
import com.example.wcf.pojo.Envios

interface EnvioModel {
    fun consultarEnvio(context: Context, idGuia: Int)
    fun cambiarEstadoEnvio(context: Context, envios: EnvioEstado)
}