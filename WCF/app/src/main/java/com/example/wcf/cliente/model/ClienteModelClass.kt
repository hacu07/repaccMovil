package com.example.wcf.cliente.model

import android.content.Context
import com.example.wcf.Util.BasicCallback
import com.example.wcf.cliente.event.ClienteEvent
import com.example.wcf.cliente.model.DAO.ClienteDAO
import com.example.wcf.pojo.Cliente
import org.greenrobot.eventbus.EventBus

class ClienteModelClass : ClienteModel {

    lateinit var mDAO: ClienteDAO

    constructor(){
        mDAO = ClienteDAO()
    }

    private fun post(clienteEvent: ClienteEvent) {
        EventBus.getDefault().post(clienteEvent)
    }

    override fun buscarCliente(context: Context, cliente: Cliente) {
        mDAO.buscarCliente(context,cliente, object: BasicCallback{
            override fun response(event: Any) {
                post(event as ClienteEvent)
            }
        })
    }

    override fun guardarCliente(context: Context, cliente: Cliente) {
        mDAO.guardarCliente(context,cliente, object: BasicCallback{
            override fun response(event: Any) {
                post(event as ClienteEvent)
            }
        })
    }

    override fun cambiarEstado(context: Context, cliente: Cliente) {
        mDAO.cambiarEstado(context,cliente, object: BasicCallback{
            override fun response(event: Any) {
                post(event as ClienteEvent)
            }
        })
    }
}