package com.example.repacc.contacto.model.DAO

import android.content.Context
import com.example.repacc.contacto.event.SolicitudEvent
import org.greenrobot.eventbus.EventBus

class SolicitudModelClass: SolicitudModel {

    private lateinit var mDAO: ContactoDAO

    constructor(){
        this.mDAO = ContactoDAO()
    }

    override fun cargarSolicitud(context: Context) {
        mDAO.cargarSolicitudes(context, object: SolicitudCallback{
            override fun response(event: SolicitudEvent) {
                post(event)
            }
        } )
    }

    private fun post(event: SolicitudEvent) {
        EventBus.getDefault().post(event)
    }
}