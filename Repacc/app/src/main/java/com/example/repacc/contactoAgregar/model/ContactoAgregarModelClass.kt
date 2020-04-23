package com.example.repacc.contactoAgregar.model

import android.content.Context
import com.example.repacc.contactoAgregar.events.ContactosEvent
import com.example.repacc.contactoAgregar.model.DAO.ContactoAgregarDAO
import com.example.repacc.pojo.Auxiliares.SolicitudRegistrar
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import org.greenrobot.eventbus.EventBus

class ContactoAgregarModelClass : ContactoAgregarModel {
    lateinit var mDAO: ContactoAgregarDAO

    constructor(){
        mDAO = ContactoAgregarDAO()
    }

    override fun buscarContacto(context: Context, username: String) {
        mDAO.buscarUsuario(context, username, object: BasicCallback{
            override fun response(event: Any) {
                postBusquedaContacto(event as ContactosEvent)
            }
        })
    }

    fun postBusquedaContacto(contactosEvent: ContactosEvent) {
        EventBus.getDefault().post(contactosEvent)
    }

    override fun registrarSolicitud(context: Context, solicitudRegistrar: SolicitudRegistrar) {
        mDAO.registrarSolicitud(context, solicitudRegistrar, object: BasicCallback{
            override fun response(event: Any) {
                postRegistroSolicitud(event as BasicEvent)
            }
        })
    }

    private fun postRegistroSolicitud(basicEvent: BasicEvent) {
        EventBus.getDefault().post(basicEvent)
    }
}