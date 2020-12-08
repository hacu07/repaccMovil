package com.example.repacc.contacto

import android.content.Context
import com.example.repacc.contacto.event.ContactoEvent
import com.example.repacc.contacto.event.EstadoSolicitudEvent
import com.example.repacc.contacto.event.SolicitudEvent
import com.example.repacc.contacto.model.SolicitudModel
import com.example.repacc.contacto.model.SolicitudModelClass
import com.example.repacc.contacto.view.ContactoActivity
import com.example.repacc.contacto.view.ContactoView
import com.example.repacc.pojo.Solicitud
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ContactoPresenterClass : ContactoPresenter{

    private var mView: ContactoView?
    private lateinit var mModel: SolicitudModel

    private var mAceptado = false // Si acepto o rechazo la solicitud
    private lateinit var mSolicitud: Solicitud

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
            mView?.mostrarProgresoSolicitudes(true)
            mView?.habilitarElementos(false)
            mModel.cargarSolicitud(context)
        }
    }

    override fun cargarContactos(context: Context) {
        if (mView != null){
            mView?.habilitarElementos(false)
            mView?.mostrarProgresoContactos(true)
            mModel.cargarContactos(context)
        }
    }

    override fun cambiarEstadoSolicitud(context: Context, solicitud: Solicitud, aceptado: Boolean) {
        if (mView != null){
            mAceptado = aceptado
            mSolicitud = solicitud
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.cambiarEstadoSolicitud(context, solicitud._id, aceptado)
        }
    }

    @Subscribe
    public fun onEventListener(event: SolicitudEvent){
        if (event != null){
            mView?.mostrarProgresoSolicitudes(false)
            mView?.habilitarElementos(true)

            when(event.typeEvent){
                Util.SUCCESS -> {
                    mView?.cargarSolicitudes(event.content)
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    event.msj?.let { msj -> mView?.mostrarMsjSolicitudes(msj) }
            }
        }
    }

    @Subscribe
    public fun listenerContacts(event: ContactoEvent){
        if (event != null){
            mView?.mostrarProgresoContactos(false)
            mView?.habilitarElementos(true)

            when(event.typeEvent){
                Util.SUCCESS -> {
                    if (event.content != null )
                        mView?.cargarContactos(event.content)
                    else
                        mView?.mostrarMsjContactos(event.msj!!)
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    event.msj?.let { msj -> mView?.mostrarMsjContactos(msj) }
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
                    if (mAceptado)
                        mView?.agregarContacto(event.content)
                    else
                        mView?.eliminarSolicitudRechazada(mSolicitud)
                }

                Util.ERROR_DATA,Util.ERROR_RESPONSE,Util.ERROR_CONEXION->
                    event.msj?.let { msj -> mView?.mostrarMsj(msj) }
            }
        }
    }
}