package com.example.repacc.registro.view

import com.example.repacc.pojo.Usuario

interface RegistroView {
    fun habilitarElementos()
    fun inhabilitarElementos()

    fun mostrarProgreso()
    fun ocultarProgreso()

    fun mostrarMsj(msj: String)

    // Si el usuario se registro correctamente
    fun irLogin(usuario: Usuario)

}