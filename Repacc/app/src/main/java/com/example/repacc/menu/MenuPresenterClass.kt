package com.example.repacc.menu

import android.content.Context
import com.example.repacc.R
import com.example.repacc.menu.events.EstadoAgenteEvent
import com.example.repacc.menu.events.NotificacionesEvent
import com.example.repacc.menu.model.DAO.SocketEvent
import com.example.repacc.menu.model.MenuModel
import com.example.repacc.menu.model.MenuModelClass
import com.example.repacc.menu.view.MenuActivity
import com.example.repacc.menu.view.MenuView
import com.example.repacc.pojo.Auxiliares.SocketUsuario
import com.example.repacc.pojo.Notificacion
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.json.JSONObject
import java.lang.Exception

class MenuPresenterClass: MenuPresenter {

    private var mView: MenuView? = null
    private var mModel: MenuModel

    private var estadoAnt = false
    private lateinit var context: Context
    private var lastSocketId: String? = null

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

    /*****************************************
     * Connect with server socket
     * HAROLDC 23/08/2020
     */
    override fun initSocket(args: Array<out Any?>) {
        try {
            //socketId
            val jsonObject = args[0] as JSONObject
            Constantes.config?.usuario?.socketId = jsonObject.optString("socketId")
            if (Constantes.config?.usuario?.socketId != null){
                val socketUsuario = SocketUsuario(
                    _id = Constantes.config?.usuario?._id!!,
                    asignar = true,
                    socketId = Constantes.config?.usuario?.socketId
                )
                lastSocketId = Constantes.config?.usuario?.socketId
                mView.let {
                    it?.mostrarMsj(socketUsuario.socketId!!)
                }
                mModel.initSocket(socketUsuario)
            }
        }catch (exc: Exception){
            //ignore this
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
        Constantes.config?.agente?.estado = event.content!!
        Util.guardarConfig(context)
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

    override fun obtenerNotificaciones(context: Context) {
        if(mView != null){
            mView?.habilitarElementos(false)
            mModel.obtenerNotificaciones(context)
        }
    }

    /******************************++
     * Get json send by API, JSON contains a notification object
     * HAROLDC 15-September-2020
     */
    override fun getNotification(args: Array<Any>) {
        try {
            val notificacion = Gson().fromJson(args[0].toString(), Notificacion::class.java)
            mView?.showNotification(notificacion)
        }catch (e: Exception){
            //mView?.mostrarMsj(e.toString())
        }
    }

    @Subscribe
    fun onEventNotificaciones(event: NotificacionesEvent){
        if (mView != null){
            mView?.habilitarElementos(true)

            when(event.typeEvent){
                Util.SUCCESS -> mView?.mostrarNotificaciones(event.content!!)
                else -> mView?.mostrarMsj(event.msj!!)
            }
        }
    }

    @Subscribe
    fun onSocketEventListener(event: SocketEvent){
        if (mView != null){
            mView?.habilitarElementos(true)

            when(event.typeEvent){
                // Se actualizo correctamente el id del socket actual
                Util.SUCCESS -> {
                    mView?.setEmitterListener(event.socketUsuario, lastSocketId)
                }
                else -> mView?.mostrarMsj(event.msj!!)
            }
        }
    }
}