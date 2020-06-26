package com.example.repacc.menu.view

interface MenuView {
    fun habilitarElementos(habilita: Boolean)
    fun mostrarMsj(msj: String)
    fun asignarEstado(estadoAnt: Boolean)
}