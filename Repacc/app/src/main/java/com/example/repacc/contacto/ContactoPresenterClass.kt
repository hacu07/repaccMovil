package com.example.repacc.contacto

import android.content.Context
import com.example.repacc.contacto.event.ContactoEvent
import com.example.repacc.contacto.event.EstadoSolicitudEvent
import com.example.repacc.contacto.event.SolicitudEvent
import com.example.repacc.contacto.model.SolicitudModel
import com.example.repacc.contacto.model.SolicitudModelClass
import com.example.repacc.contacto.view.ContactoActivity
import com.example.repacc.contacto.view.ContactoView
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ContactoPresenterClass : ContactoPresenter{

    private var mView: ContactoView?
    private lateinit var mModel: SolicitudModel

    constructor(mView: ContactoActivity){
        this.mView = mView
        this.mModel = SolicitudModelClass()
    }

    override fun onCreate() {
        if (mView != null){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        if (mView != null){
            EventBus.getDefault().unregister(this)
            mView = null
        }
    }

    override fun cargarSolicitudes(context: Context) {
        if (mView != null){
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.cargarSolicitud(context)
        }
    }

    override fun cargarContactos(context: Context) {
        if (mView != null){
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.cargarContactos(context)
        }
    }

    override fun cambiarEstadoSolicitud(context: Context, idSolicitud: String, aceptado: Boolean) {
        if (mView != null){
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.cambiarEstadoSolicitud(context, idSolicitud, aceptado)
        }
    }

    @Subscribe
    public fun onEventListener(event: SolicitudEvent){
        if (event != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)

            when(event.typeEvent){
                Util.SUCCESS -> {
                    mView?.cargarSolicitudes(event.content)
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    event.msj?.let { msj -> mView?.mostrarMsj(msj) }
            }
        }
    }

    @Subscribe
    public fun listenerContacts(event: ContactoEvent){
        if (event != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)

            when(event.typeEvent){
                Util.SUCCESS -> {
                    mView?.cargarContactos(event.content)
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    event.msj?.let { msj -> mView?.mostrarMsj(msj) }
            }
        }
    }

    @Subscribe
    public fun listenerChangeStated(event: EstadoSolicitudEvent){
        if (event != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)

            when(event.typeEvent){
                Util.SUCCESS -> {
                    mView?.agregarContacto(event.content)
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    event.msj?.let { msj -> mView?.mostrarMsj(msj) }
            }
        }
    }
}