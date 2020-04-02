package com.example.repacc.contacto.model.DAO

import com.example.repacc.contacto.event.ContactoEvent

interface ContactoCallback {
    fun response(response: ContactoEvent)
}