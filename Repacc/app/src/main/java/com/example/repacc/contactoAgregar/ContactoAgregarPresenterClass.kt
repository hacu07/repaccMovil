package com.example.repacc.contactoAgregar

import android.content.Context
import androidx.appcompat.view.menu.MenuView
import com.example.repacc.contactoAgregar.events.ContactosEvent
import com.example.repacc.contactoAgregar.model.ContactoAgregarModel
import com.example.repacc.contactoAgregar.model.ContactoAgregarModelClass
import com.example.repacc.contactoAgregar.view.ContactoAgregarActivity
import com.example.repacc.contactoAgregar.view.ContactoAgregarView
import com.example.repacc.pojo.Auxiliares.SolicitudRegistrar
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ContactoAgregarPresenterClass : ContactoAgregarPresenter {

    var mView : ContactoAgregarView?
    lateinit var mModel: ContactoAgregarModel

    constructor(mView: ContactoAgregarActivity){
        this.mView = mView
        mModel = ContactoAgregarModelClass()
    }

    override fun onCreate() {
        if (mView != null){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    override fun buscarContacto(context: Context, username: String) {
        if (mView != null){
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.buscarContacto(context, username)
        }
    }

    override fun registrarSolicitud(context: Context, usuario: Usuario) {
        if (mView != null){
            val solicitudRegistrar = SolicitudRegistrar(
                usuario = Constantes.config?.usuario?._id!!,
                contacto = usuario._id!!
            )
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.registrarSolicitud(context, solicitudRegistrar)
        }
    }

    @Subscribe
    fun onEventListenerBusqueda(event :ContactosEvent){
        if (event!= null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)

            when(event.typeEvent){
                Util.SUCCESS -> mView?.cargarUsuarios(event.content!!)
                else -> mView?.mostrarMsj(event.msj!!)
            }
        }
    }

    @Subscribe
    fun onEventListenerRegistro(event :BasicEvent){
        if (event!= null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)

            mView?.mostrarMsj(event.msj!!)

            when(event.typeEvent){
                Util.SUCCESS -> mView?.limpiar()
            }
        }
    }
}