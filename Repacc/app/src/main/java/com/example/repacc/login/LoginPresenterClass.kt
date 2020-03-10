package com.example.repacc.login

import android.content.Context
import com.example.repacc.login.event.LoginEvent
import com.example.repacc.login.model.LoginInteractor
import com.example.repacc.login.model.LoginInteractorClass
import com.example.repacc.login.view.LoginActivity
import com.example.repacc.login.view.LoginView
import com.example.repacc.pojo.Usuario
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class LoginPresenterClass: LoginPresenter {

    var mView: LoginView?
    var mModel: LoginInteractor

    constructor(mView: LoginActivity){
        this.mView = mView
        mModel = LoginInteractorClass()
    }

    override fun validarUsuario(context: Context, usuario: Usuario) {
        mView?.let {
            it.mostrarDialogo()
            it.inhabilitarElementos()
            mModel.validarUsuario(context,usuario)
        }
    }

    override fun onCreate() {
        mView?.let {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    @Subscribe
    fun onEventListenerLogin(loginEvent: LoginEvent) {
        mView?.let {
            it.habilitarElementos()
            it.ocultarDialogo()

            when(loginEvent.typeEvent){
                Util.SUCCESS -> it.irInicio()
                Util.ERROR_RESPONSE,Util.ERROR_DATA, Util.ERROR_CONEXION -> it.mostrarMsj(loginEvent.msj)
            }
        }
    }
}