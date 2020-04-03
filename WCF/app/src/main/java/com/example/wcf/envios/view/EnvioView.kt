package com.example.wcf.envios.view

import com.example.wcf.envios.events.EnvioEvent
import com.example.wcf.pojo.Envios

interface EnvioView {
    fun habilitarElementos(habilita :Boolean)
    fun mostrarProgreso(habilita :Boolean)
    fun habilitarFormulario(habilita :Boolean)
    fun mostrarMsj(msj: String)
    fun limpiar()
    fun cargarDatosEnvio(content: Envios?)
}