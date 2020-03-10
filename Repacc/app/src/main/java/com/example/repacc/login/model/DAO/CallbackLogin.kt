package com.example.repacc.login.model.DAO

import com.example.repacc.login.event.LoginEvent
import com.example.repacc.registro.event.RegistroEvent

interface CallbackLogin {
    fun onSuccess(loginEvent: LoginEvent)
    fun onError(loginEvent: LoginEvent)
}