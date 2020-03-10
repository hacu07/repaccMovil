package com.example.repacc.login.model

import android.content.Context
import com.example.repacc.pojo.Usuario

interface LoginInteractor {
    fun validarUsuario(context: Context, usuario: Usuario)
}