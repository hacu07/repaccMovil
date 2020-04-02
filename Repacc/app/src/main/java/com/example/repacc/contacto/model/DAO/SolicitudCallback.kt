package com.example.repacc.contacto.model.DAO

import com.example.repacc.contacto.event.SolicitudEvent

interface SolicitudCallback {
    fun  response(event: SolicitudEvent)
}