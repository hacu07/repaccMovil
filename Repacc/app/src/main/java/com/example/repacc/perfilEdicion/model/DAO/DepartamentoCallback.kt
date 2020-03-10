package com.example.repacc.perfilEdicion.model.DAO

import com.example.repacc.perfilEdicion.event.DepartamentoEvent

interface DepartamentoCallback {
    fun response(departamentoEvent: DepartamentoEvent)
}