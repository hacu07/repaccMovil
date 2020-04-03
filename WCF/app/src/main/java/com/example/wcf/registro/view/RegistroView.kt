package com.example.wcf.registro.view

import com.example.wcf.pojo.Cliente

interface RegistroView {
    fun salir()
    fun limpiar()
    fun habilitarElementos(habilita: Boolean)
    fun mostrarProgreso(muestra: Boolean)
    fun mostrarMsj(msj: String)
    fun mostrarErrorPeso(msj: String)
    fun mostrarErrorValorEnvio(msj: String)
    fun mostrarErrorSeguro(msj: String)
    fun mostarErrorClienteEmisor(msj: String)
    fun mostarErrorClienteDestino(msj: String)
    fun mostarErrorCiudadOrigen(msj: String)
    fun mostarErrorCiudadDestino(msj: String)
    fun cargarCiudadesOrigen(ciudadesOrigen: List<String>)
    fun cargarCiudadesDestino(lCiudadesdestino: List<String>)
    fun asignarNombreCliente(content: Cliente?, tipoCliente: Int)
    fun limpiarNombreCliente(tipoCliente: Int)
}