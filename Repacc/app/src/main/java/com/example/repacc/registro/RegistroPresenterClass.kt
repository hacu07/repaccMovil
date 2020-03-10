package com.example.repacc.registro

import com.example.repacc.pojo.Usuario
import com.example.repacc.registro.event.RegistroEvent
import com.example.repacc.registro.model.RegistroInteractor
import com.example.repacc.registro.model.RegistroInteractorClass
import com.example.repacc.registro.view.RegistroActivity
import com.example.repacc.registro.view.RegistroView
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class RegistroPresenterClass: RegistroPresenter {
    var mView:RegistroView? = null
    var mModel:RegistroInteractor? = null

    constructor(registroActivity: RegistroActivity){
        this.mView = registroActivity
        this.mModel = RegistroInteractorClass()
    }

    override fun onCreate() {
        if (mView != null) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        mView = null
        EventBus.getDefault().unregister(this)
    }

    override fun registrarUsuario(usuario: Usuario) {
        if (mView != null){
            mView?.inhabilitarElementos()
            mView?.mostrarProgreso()

            mModel?.registrarUsuario(usuario)
        }
    }

    @Subscribe
    fun onEventListener(registroEvent: RegistroEvent) {
        if (registroEvent != null){

            mView?.habilitarElementos()
            mView?.ocultarProgreso()

            when(registroEvent.typeEvent){
                Util.SUCCESS -> registroEvent.msj?.let { mView?.mostrarMsj(it) }
                Util.ERROR_RESPONSE,Util.ERROR_DATA, Util.ERROR_CONEXION ->{
                    registroEvent?.msj?.let { mView?.mostrarMsj(it) }
                }
            }
        }
    }
}