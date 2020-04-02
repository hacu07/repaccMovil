package com.example.wcf.registro.model

import android.content.Context
import com.example.wcf.pojo.Cliente
import com.example.wcf.pojo.Envios

interface RegistroModel {
    fun registrarEnvio(context: Context, envios: Envios)
    fun buscarCliente(context: Context, cliente: Cliente)
    fun obtenerPesos(context: Context)
}