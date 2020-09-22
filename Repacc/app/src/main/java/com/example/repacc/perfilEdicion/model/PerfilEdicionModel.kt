package com.example.repacc.perfilEdicion.model

import android.content.Context
import android.net.Uri
import com.example.repacc.pojo.Usuario

interface PerfilEdicionModel {
    fun obtenerPaises(context: Context)

    fun obtenerDepartamentos(context: Context, idPais: String)

    fun obtenerCiudades(context: Context, idDpto: String)

    fun editarPerfil(
        context: Context,
        usuario: Usuario,
        mPhotoSelectedUri: Uri?
    )
}