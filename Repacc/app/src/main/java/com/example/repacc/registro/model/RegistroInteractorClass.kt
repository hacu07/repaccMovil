package com.example.repacc.registro.model

import com.example.repacc.pojo.Usuario
import com.example.repacc.registro.event.RegistroEvent
import com.example.repacc.registro.model.DAO.CallbackRegistro
import com.example.repacc.registro.model.DAO.RegistroDAO
import org.greenrobot.eventbus.EventBus

class RegistroInteractorClass: RegistroInteractor, CallbackRegistro {

    var registroDAO:RegistroDAO? = null


    constructor(){
        registroDAO = RegistroDAO()
    }

    override fun registrarUsuario(usuario: Usuario) {
        registroDAO?.registrarUsuario(usuario,this )
    }


    /**
     * Callback de registro
     * */
    override fun onSuccess(registroEvent: RegistroEvent) {
        postRegistroUsuario(registroEvent)
    }

    override fun onError(registroEvent: RegistroEvent) {
        postRegistroUsuario(registroEvent)
    }

    fun postRegistroUsuario(registroEvent: RegistroEvent){
        EventBus.getDefault().post(registroEvent)
    }
}