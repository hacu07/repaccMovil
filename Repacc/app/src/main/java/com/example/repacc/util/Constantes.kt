package com.example.repacc.util

import com.example.repacc.pojo.Config

class Constantes {

    companion object{
        // Preferencias
        val TAG_CONFIG: String = "CONFIG_PREFERENCE"
        val PREFERENCE_PRIVATE_MODE: Int = 0
        val PREFERENCES_CONFIG: String = "CONFIG_PREFERENCE_TAG"

        // Tag de Header obtenido al iniciar sesion
        val HEADER_AUTHORIZATION = "Authorization"

        // Configuracion a usar en la APP
        var config: Config? = null
    }
}