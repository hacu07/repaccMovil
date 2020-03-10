package com.example.repacc.perfilEdicion.model.DAO

import com.example.repacc.perfilEdicion.event.PaisEvent

interface PaisCallback {
    fun response(paisEvent: PaisEvent)
}