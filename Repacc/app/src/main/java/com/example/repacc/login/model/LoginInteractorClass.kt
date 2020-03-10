package com.example.repacc.login.model

import android.content.Context
import com.example.repacc.login.event.LoginEvent
import com.example.repacc.login.model.DAO.CallbackLogin
import com.example.repacc.login.model.DAO.LoginDAO
import com.example.repacc.pojo.Usuario
import org.greenrobot.eventbus.EventBus

class LoginInteractorClass : LoginInteractor, CallbackLogin {

    lateinit var mDao: LoginDAO

    constructor(){
        mDao = LoginDAO()
    }

    override fun validarUsuario(context: Context, usuario: Usuario) {
        mDao.validarSesion(context, usuario, this);
    }

    /***********************************************************************
        CALLBACK LOGIN
     **********************************************************************/
    override fun onSuccess(loginEvent: LoginEvent) {
        postLogin(loginEvent)
    }

    override fun onError(loginEvent: LoginEvent) {
        postLogin(loginEvent)
    }


    private fun postLogin(loginEvent: LoginEvent) {
        EventBus.getDefault().post(loginEvent)
    }
}