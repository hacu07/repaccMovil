package com.example.wcf.registro.model

import android.content.Context
import com.example.wcf.Util.BasicCallback
import com.example.wcf.pojo.Cliente
import com.example.wcf.pojo.Envios
import com.example.wcf.registro.event.ClienEvent
import com.example.wcf.registro.event.PesosEvent
import com.example.wcf.registro.event.RegistroEvent
import com.example.wcf.registro.model.DAO.RegistroDAO
import org.greenrobot.eventbus.EventBus

class RegistroModelClass : RegistroModel {

    lateinit var mDAO: RegistroDAO

    constructor(){
        mDAO = RegistroDAO()
    }

    override fun registrarEnvio(context: Context, envios: Envios) {
        mDAO.registrarEnvio(context,envios,object: BasicCallback{
            override fun response(event: Any) {
                postRegistroEnvio(event as RegistroEvent)
            }
        })
    }

    private fun postRegistroEnvio(registroEvent: RegistroEvent) {
        EventBus.getDefault().post(registroEvent)
    }

    override fun buscarCliente(context: Context, cliente: Cliente) {
        mDAO.buscarCliente(context,cliente,object: BasicCallback{
            override fun response(event: Any) {
                postBusquedaCliente(event as ClienEvent)
            }
        })
    }

    private fun postBusquedaCliente(clienEvent: ClienEvent) {
        EventBus.getDefault().post(clienEvent)
    }

    override fun obtenerPesos(context: Context) {
        mDAO.obtenerPesos(context, object: BasicCallback{
            override fun response(event: Any) {
                postPesos(event as PesosEvent)
            }
        })
    }

    private fun postPesos(pesosEvent: PesosEvent) {
        EventBus.getDefault().post(pesosEvent)
    }
}