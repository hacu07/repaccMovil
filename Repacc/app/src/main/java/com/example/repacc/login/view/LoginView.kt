package com.example.repacc.login.view

interface LoginView {
    fun habilitarElementos()
    fun inhabilitarElementos()

    fun mostrarDialogo()
    fun ocultarDialogo()

    fun mostrarMsj(msj: String)

    fun irInicio()
}