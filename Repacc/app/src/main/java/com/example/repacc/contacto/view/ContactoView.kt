package com.example.repacc.contacto.view

import com.example.repacc.pojo.Contacto
import com.example.repacc.pojo.Solicitud
import com.example.repacc.pojo.Usuario

interface ContactoView {
    fun habilitarElementos(siHabilita : Boolean)
    fun mostrarProgreso(siMuestra: Boolean)
    fun mostrarProgresoSolicitudes(siMuestra: Boolean)
    fun mostrarProgresoContactos(siMuestra: Boolean)
    fun mostrarMsj(msj : String)
    fun mostrarMsjSolicitudes(msj : String)
    fun mostrarMsjContactos(msj : String)


    fun cargarSolicitudes(solicitudes: ArrayList<Solicitud>?)
    fun cargarContactos(content: ArrayList<Contacto>?)

    fun agregarContacto(content: Contacto?)
    fun eliminarSolicitudRechazada(solicitud: Solicitud)

}