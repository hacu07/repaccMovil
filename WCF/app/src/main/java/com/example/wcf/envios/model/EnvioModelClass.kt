package com.example.wcf.envios.model

import android.content.Context
import com.example.wcf.Util.BasicCallback
import com.example.wcf.envios.events.EnvioEvent
import com.example.wcf.envios.events.EstadoEvent
import com.example.wcf.envios.model.DAO.EnvioDAO
import com.example.wcf.pojo.EnvioEstado
import com.example.wcf.pojo.Envios
import org.greenrobot.eventbus.EventBus

class EnvioModelClass: EnvioModel {
    lateinit var mDAO: EnvioDAO

    constructor(){
        mDAO = EnvioDAO()
    }

    override fun consultarEnvio(context: Context, idGuia: Int) {
        mDAO.consultarEnvio(context,idGuia, object : BasicCallback{
            override fun response(event: Any) {
                postConsultaEnvio(event as EnvioEvent)
            }
        })
    }

    private fun postConsultaEnvio(envioEvent: EnvioEvent) {
        EventBus.getDefault().post(envioEvent)
    }

    override fun cambiarEstadoEnvio(context: Context, envios: EnvioEstado) {
        mDAO.cambiarEstadoEnvio(context,envios, object: BasicCallback{
            override fun response(event: Any) {
                postCambioEstado(event as EstadoEvent)
            }
        })
    }

    private fun postCambioEstado(estadoEvent: EstadoEvent) {
        EventBus.getDefault().post(estadoEvent)
    }
}