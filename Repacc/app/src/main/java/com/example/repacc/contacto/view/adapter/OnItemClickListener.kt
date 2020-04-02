package com.example.repacc.contacto.view.adapter

import com.example.repacc.pojo.Solicitud
import com.example.repacc.pojo.Usuario

interface OnItemSolicitudListener {
    /*
    * Si el usuario acepto o rechazo la solicitud
    * */
    fun listenerEstadoSolicitud(solicitud: Solicitud, aceptado: Boolean)
}