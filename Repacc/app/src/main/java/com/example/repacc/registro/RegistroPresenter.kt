package com.example.repacc.registro

import com.example.repacc.pojo.Usuario
import com.example.repacc.registro.event.RegistroEvent

interface RegistroPresenter {
    fun onCreate()
    fun onDestroy()

    fun registrarUsuario(usuario: Usuario)

}