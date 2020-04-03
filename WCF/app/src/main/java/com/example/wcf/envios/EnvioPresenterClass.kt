package com.example.wcf.envios

import android.content.Context
import com.example.wcf.Util.Util
import com.example.wcf.envios.events.EnvioEvent
import com.example.wcf.envios.events.EstadoEvent
import com.example.wcf.envios.model.EnvioModel
import com.example.wcf.envios.model.EnvioModelClass
import com.example.wcf.envios.view.EnvioView
import com.example.wcf.envios.view.EnviosActivity
import com.example.wcf.pojo.EnvioEstado
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class EnvioPresenterClass: EnvioPresenter {

    var mView: EnvioView? = null
    lateinit var mModel: EnvioModel

    constructor(mView: EnviosActivity){
        this.mView = mView
        mModel = EnvioModelClass()
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

    override fun consultarEnvio(context: Context, idGuia: Int) {
        if (mView != null && idGuia > 0){
            mView?.habilitarElementos(false)
            mView?.mostrarProgreso(true)
            mView?.habilitarFormulario(false)
            mModel.consultarEnvio(context,idGuia)
        }
    }

    override fun cambiarEstado(context: Context,idGuia: Int, cod_estado: Int) {
        if (mView != null && idGuia <= 0){
            mView?.habilitarElementos(false)
            mView?.mostrarProgreso(true)
            mView?.habilitarFormulario(false)
            val envios = EnvioEstado(cod_guia = idGuia, cod_estado = cod_estado)
            mModel.cambiarEstadoEnvio(context,envios)
        }
    }

    @Subscribe
    fun onEventConsultaEnvio(event: EnvioEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)
            event.msj?.let { mView?.mostrarMsj(it) }
            when(event.typeEvent){
                Util.SUCCESS -> {
                    mView?.habilitarFormulario(true)
                    mView?.cargarDatosEnvio(event.content)
                }
                else ->{
                    mView?.limpiar()
                }
            }
        }
    }

    @Subscribe
    fun onEventCambioEstado(event: EstadoEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)
            event.msj?.let { mView?.mostrarMsj(it) }
            when(event.typeEvent){
                Util.SUCCESS -> {
                    mView?.limpiar()
                }
                else ->{
                    mView?.habilitarFormulario(true)
                }
            }
        }
    }
}