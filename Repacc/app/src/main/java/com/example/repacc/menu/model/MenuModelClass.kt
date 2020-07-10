package com.example.repacc.menu.model

import android.content.Context
import com.example.repacc.menu.events.EstadoAgenteEvent
import com.example.repacc.menu.events.NotificacionesEvent
import com.example.repacc.menu.model.DAO.DAO
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import org.greenrobot.eventbus.EventBus

class MenuModelClass : MenuModel {

    private lateinit var mDAO : DAO

    init {
        mDAO = DAO()
    }

    override fun cambiarEstado(context: Context, disponible: Boolean) {
        mDAO.cambiarEstado(context, disponible, object : BasicCallback{
            override fun response(event: Any) {
                postEstado(event as EstadoAgenteEvent)
            }
        })
    }

    fun postEstado(event: EstadoAgenteEvent) {
        EventBus.getDefault().post(event)
    }

    override fun obtenerNotificaciones(context: Context) {
        mDAO.obtenerNotificaciones(context, object : BasicCallback {
            override fun response(event: Any) {
                postNotificaciones(event as NotificacionesEvent)
            }
        })
    }

    fun postNotificaciones(event: NotificacionesEvent) {
        EventBus.getDefault().post(event)
    }
}