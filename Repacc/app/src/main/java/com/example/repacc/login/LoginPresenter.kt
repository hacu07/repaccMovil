package com.example.repacc.login

import android.content.Context
import com.example.repacc.login.event.LoginEvent
import com.example.repacc.pojo.Usuario

interface LoginPresenter {
    fun validarUsuario(context: Context, usuario: Usuario)

    fun onCreate()
    fun onDestroy()
}