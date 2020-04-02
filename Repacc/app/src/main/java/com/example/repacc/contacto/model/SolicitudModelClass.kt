package com.example.repacc.contacto.model

import android.content.Context
import com.example.repacc.contacto.event.ContactoEvent
import com.example.repacc.contacto.event.EstadoSolicitudEvent
import com.example.repacc.contacto.event.SolicitudEvent
import com.example.repacc.contacto.model.DAO.ContactoCallback
import com.example.repacc.contacto.model.DAO.ContactoDAO
import com.example.repacc.contacto.model.DAO.SolicitudCallback
import com.example.repacc.util.BasicCallback
import org.greenrobot.eventbus.EventBus

class SolicitudModelClass: SolicitudModel {

    private lateinit var mDAO: ContactoDAO

    constructor(){
        this.mDAO = ContactoDAO()
    }

    override fun cargarSolicitud(context: Context) {
        mDAO.cargarSolicitudes(context, object:
            SolicitudCallback {
            override fun response(event: SolicitudEvent) {
                post(event)
            }
        } )
    }

    override fun cargarContactos(context: Context) {
        mDAO.cargarContactos(context, object: ContactoCallback{
            override fun response(event: ContactoEvent) {
                postContacto(event)
            }
        })
    }

    override fun cambiarEstadoSolicitud(context: Context, idSolicitud: String, aceptado: Boolean) {
        mDAO.cambiarEstado(context, idSolicitud, aceptado, object: BasicCallback{
            override fun response(event: Any) {
                postCambiarEstado(event as EstadoSolicitudEvent)
            }

        })
    }

    private fun postCambiarEstado(event: EstadoSolicitudEvent) {
        EventBus.getDefault().post(event)
    }

    private fun postContacto(event: ContactoEvent) {
        EventBus.getDefault().post(event)
    }

    private fun post(event: SolicitudEvent) {
        EventBus.getDefault().post(event)
    }


}