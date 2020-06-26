package com.example.repacc.menu

import android.content.Context
import com.example.repacc.R
import com.example.repacc.menu.events.EstadoAgenteEvent
import com.example.repacc.menu.model.MenuModel
import com.example.repacc.menu.model.MenuModelClass
import com.example.repacc.menu.view.MenuActivity
import com.example.repacc.menu.view.MenuView
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MenuPresenterClass: MenuPresenter {

    private var mView: MenuView? = null
    private lateinit var mModel: MenuModel

    private var estadoAnt = false
    private lateinit var context: Context

    constructor(mView: MenuActivity){
        this.mView = mView
        mModel = MenuModelClass()
    }

    override fun onCreate() {
        if (mView != null){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    override fun cambiarEstado(context: Context, disponible: Boolean) {
        if (mView != null){
            estadoAnt = !disponible
            this.context = context
            mView?.habilitarElementos(false)
            mModel.cambiarEstado(context, disponible)
        }
    }

    @Subscribe
    fun onEventListener(event: EstadoAgenteEvent){
        if (mView != null){
            mView?.habilitarElementos(true)

            when(event.typeEvent){
                Util.SUCCESS -> eventSuccess(event)
                else -> eventError()
            }
        }
    }

    private fun eventSuccess(event: EstadoAgenteEvent) {
        mView?.mostrarMsj(event.msj!!)
        // Asigna estado a agente
        Constantes.config?.agente?.estado = event.content
        mView?.asignarEstado(Constantes.config?.agente?.estado?.codigo == Constantes.ESTADO_CODIGO_ACTIVO)
    }

    /*
    * No se logro actualizar estado, Muestra msj y no cambia estado anterior
    * HAROLDC 23/06/2020
    * */
    private fun eventError() {
        mView?.mostrarMsj(context.getString(R.string.no_actualizo_estado_agente))
        mView?.asignarEstado(estadoAnt)
    }
}