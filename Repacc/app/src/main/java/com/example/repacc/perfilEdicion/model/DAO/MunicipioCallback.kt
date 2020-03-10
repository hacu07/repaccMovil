package com.example.repacc.perfilEdicion.model.DAO

import com.example.repacc.perfilEdicion.event.MunicipioEvent

interface MunicipioCallback {
    fun response(municipioEvent: MunicipioEvent)
}