package com.example.repacc.registro.model.DAO

import com.example.repacc.registro.event.RegistroEvent

interface CallbackRegistro {
    fun onSuccess(registroEvent:RegistroEvent)
    fun onError(registroEvent: RegistroEvent)
}