package com.example.wcf.cliente.vista

import com.example.wcf.pojo.Cliente

interface ClienteView {

    fun habilitarElementos(habilita: Boolean)
    fun mostrarProgreso(habilita: Boolean)
    fun mostrarEstado(muestra: Boolean)
    fun habilitaFormulario(habilita: Boolean)
    fun habilitarBusqueda(habilita: Boolean)

    fun mostrarMsj(msj: String)

    fun cargarCliente(cliente: Cliente)

    fun limpiarCampos()
}