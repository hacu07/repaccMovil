package com.example.repacc.registro.model

import com.example.repacc.pojo.Usuario

interface RegistroInteractor {
    fun registrarUsuario(usuario: Usuario)
}