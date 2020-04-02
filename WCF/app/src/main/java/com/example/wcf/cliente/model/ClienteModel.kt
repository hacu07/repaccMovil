package com.example.wcf.cliente.model

import android.content.Context
import com.example.wcf.pojo.Cliente

interface ClienteModel {
    fun buscarCliente(context: Context, cliente: Cliente)
    fun guardarCliente(context: Context, cliente: Cliente)
    fun cambiarEstado(context: Context, cliente: Cliente)
}