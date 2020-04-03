package com.example.wcf.registro

import android.content.Context
import com.example.wcf.pojo.Cliente

interface RegistroPresenter {
    fun onCreate()
    fun onDestroy()

    fun asignarCiudadDestino(posicion: Int)
    fun cargarCiudadesDestino(posicion : Int)
    fun obtenerPesos(context: Context)
    fun buscarCliente(context: Context, cliente: Cliente, tipoCliente: Int)
    fun registrarEnvio(context: Context, pesoKilos: Int, valorSeguro: Int, valorEnvio: Int)
    fun pesosKl(): Int
}