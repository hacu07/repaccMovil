package com.example.repacc.contacto.view

import com.example.repacc.pojo.Contacto
import com.example.repacc.pojo.Solicitud
import com.example.repacc.pojo.Usuario

interface ContactoView {
    fun habilitarElementos(siHabilita : Boolean)
    fun mostrarProgreso(siMuestra: Boolean)
    fun mostrarMsj(msj : String)


    fun cargarSolicitudes(solicitudes: ArrayList<Solicitud>?)
    fun cargarContactos(content: ArrayList<Contacto>?)

    abstract fun agregarContacto(content: Contacto?)


}