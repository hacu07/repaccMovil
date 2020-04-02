package com.example.wcf.cliente

import android.content.Context
import com.example.wcf.Util.Util
import com.example.wcf.cliente.event.ClienteEvent
import com.example.wcf.cliente.model.ClienteModel
import com.example.wcf.cliente.model.ClienteModelClass
import com.example.wcf.cliente.vista.ClienteActivity
import com.example.wcf.cliente.vista.ClienteView
import com.example.wcf.pojo.Cliente
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ClientePresenterClass : ClientePresenter {

    var mView :ClienteView?
    lateinit var mModel: ClienteModel

    constructor(mView: ClienteActivity){
        this.mView = mView
        mModel = ClienteModelClass()
    }

    /*
    * Busca el cliente por Cedula
    * */
    override fun buscarCliente(context: Context, cliente: Cliente) {
        if (mView != null){
            if (cliente != null){
                cliente?.cedula.let {
                    if (!it.isNullOrEmpty()){
                        mView?.habilitarElementos(false)
                        mView?.mostrarProgreso(true)
                        mView?.mostrarEstado(false)
                        mModel.buscarCliente(context, cliente)
                    }else{
                        mView?.mostrarMsj("Ingrese la cedula a buscar")
                    }
                }
            }else{
                mView?.mostrarMsj("Ingrese la cedula a buscar")
            }
        }
    }


    override fun guardarCliente(context: Context, cliente: Cliente) {
        if (cliente != null){
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.guardarCliente(context,cliente)
        }
    }

    override fun cambiarEstado(context: Context, cliente: Cliente) {
        mView?.habilitarElementos(false)
        mView?.mostrarProgreso(true)
        mModel.cambiarEstado(context, cliente)
    }

    override fun onCreate() {
        if (mView != null){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView=null
    }

    @Subscribe
    fun onEventListener(event: ClienteEvent){
        if (event != null && mView != null){
            mView?.mostrarProgreso(false)
            when(event.event){
                Util.CLIENTE_EVENT_BUSQUEDA -> eventBusqueda(event)
                Util.CLIENTE_EVENT_REGISTRO -> eventRegistro(event)
                Util.CLIENTE_EVENT_ESTADO -> eventEstado(event)
            }
        }
    }

    private fun eventEstado(event: ClienteEvent) {
        event.msj?.let { mView?.mostrarMsj(it) }
        mView?.habilitaFormulario(true)
    }

    private fun eventRegistro(event: ClienteEvent) {
        when(event.typeEvent){
            Util.SUCCESS -> {
                mView?.limpiarCampos()
            }
            else -> {
                mView?.habilitaFormulario(true)
            }
        }
    }

    private fun eventBusqueda(event: ClienteEvent) {
        event.msj?.let { mView?.mostrarMsj(it) }
        when(event.typeEvent){

            Util.SUCCESS ->{
                event.content?.let { mView?.cargarCliente(it) }
            }
            Util.ERROR_DATA->{
                mView?.mostrarMsj("Puede registrar el usuario")
                mView?.habilitarBusqueda(false)
                mView?.habilitaFormulario(true)
            }
            else ->{
                mView?.limpiarCampos()
            }
        }
    }
}