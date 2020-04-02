package com.example.wcf.cliente

import android.content.Context
import com.example.wcf.pojo.Cliente

interface ClientePresenter {

    fun buscarCliente(context: Context, cliente: Cliente)
    fun guardarCliente(context: Context, cliente: Cliente)
    fun cambiarEstado(context: Context, cliente: Cliente)

    fun onCreate()
    fun onDestroy()
}