package com.example.repacc.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.repacc.pojo.Config
import com.example.repacc.vehiculo.view.VehiculoActivity

class Constantes {

    companion object{
        val ESTADO_CODIGO_ACTIVO = "T1A"
        val ESTADO_CODIGO_INACTIVO = "T1I"

        // Actividad (Arreglar desps)
        var vehiculoAgregar: VehiculoActivity? = null
        // Preferencias
        val TAG_CONFIG: String = "CONFIG_PREFERENCE"
        val PREFERENCE_PRIVATE_MODE: Int = 0
        val PREFERENCES_CONFIG: String = "CONFIG_PREFERENCE_TAG"

        // Permisos
        val LOCATION_PERMISSION_REQUEST_CODE = 10

        // Tag de Header obtenido al iniciar sesion
        val HEADER_AUTHORIZATION = "Authorization"

        // Configuracion a usar en la APP
        var config: Config? = null
    }
}