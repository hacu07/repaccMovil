package com.example.repacc.perfilEdicion.model.DAO

import com.example.repacc.perfilEdicion.event.EdicionPerfilEvent

interface EdicionPerfilCallback {
    fun response(event: EdicionPerfilEvent)
}
