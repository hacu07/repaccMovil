package com.example.repacc.reporteDetalle

import android.content.Context
import com.example.repacc.pojo.Auxiliares.Detalle
import com.example.repacc.pojo.Auxiliares.EstadoAgenRepo
import com.example.repacc.pojo.Entidad
import com.example.repacc.pojo.Estado
import com.example.repacc.pojo.Reporte
import com.example.repacc.pojo.Servicio
import com.example.repacc.reporteDetalle.model.ReporteDetalleModel
import com.example.repacc.reporteDetalle.model.ReporteDetalleModelClass
import com.example.repacc.reporteDetalle.view.ReporteDetalleActivity
import com.example.repacc.reporteDetalle.view.ReporteDetalleView
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ReporteDetallePresenterClass: ReporteDetallePresenter {

    private lateinit var mReporte: Reporte
    private var mView: ReporteDetalleView? = null
    private lateinit var mModel: ReporteDetalleModel
    private lateinit var estados : ArrayList<Estado>
    private val listaEstados = mutableListOf<String>()

    constructor(mView: ReporteDetalleActivity, reporte: Reporte){
        this.mView = mView
        mModel = ReporteDetalleModelClass()
        this.mReporte = reporte

        validarEstado()
    }

    /*
    * Si es un agente y no se ha definido si es falsa alarma el reporte,
    * solicita al agente confirmación
    * */
    private fun validarEstado() {
        mReporte.let {
            if (it.agenteFalAlarm == null && Constantes.config!!.agente != null){
                mView?.validarFalsaAlarma()
            }
        }
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

    override fun actualizarEstadoReporte(context: Context, reporte: Reporte) {
        mView.let {
            // Obtiene el estado ingresado por el agente
            mReporte.esFalAlarm = reporte.esFalAlarm
            // Asigna agente que actualizo estado del reporte
            reporte.agenteFalAlarm = Constantes.config!!.agente
            mModel.actualizarEstadoReporte(context,reporte)
        }
    }

    @Subscribe
    fun onEventListener(event: BasicEvent){
        if (event != null && mView != null){
            mView?.mostrarMsj(event.msj!!)
            when(event.typeEvent){
                Util.SUCCESS -> {
                    // Se actualizo correctamente
                    mReporte.agenteFalAlarm = Constantes.config!!.agente
                    mView?.setReporte(mReporte)
                }
                else -> {
                    // No se logró actualizar
                    // Retorna al estado antes de actualizar
                    mReporte.esFalAlarm = !mReporte.esFalAlarm
                    mView?.setReporte(mReporte)
                }
            }
        }
    }

    override fun obtenerEstadosServicios(context: Context) {
        if (mView != null){
            mModel.obtenerEstadosServicios(context)
        }
    }

    @Subscribe
    fun onEventEstadosListener(event: EstadoEvent){
        if (event != null && mView != null){
            when(event.typeEvent){
                Util.SUCCESS -> reloadServices(event.content)
            }
        }
    }

    private fun reloadServices(estados: ArrayList<Estado>?) {
        this.estados = estados!!
        estados?.forEach { estado ->
            listaEstados.add(estado.nombre!!)
        }

        mView?.let {
            it.reloadServices(listaEstados, estados)
        }
    }

    /***************************************
     * Actualiza estado agen repo
     */
    override fun actualizarEstadoAgenRepo(
        context: Context,
        position: Int,
        servicio: Servicio,
        entidad: Entidad?,
        descriptraslado: String?
    ) {
        if (mView != null){
            val detalle = Detalle(
                estado = estados[position],
                latitud = Constantes.config?.agente?.latitud!!,
                longitud = Constantes.config?.agente?.longitud!!
            )

            var detalles = ArrayList<Detalle>()
            detalles.add(detalle)

            val estadoAgenRepo = EstadoAgenRepo(
                servicio = servicio,
                agente = Constantes.config?.agente!!,
                detalle = detalles,
                estado = estados[position],
                unidadMedica = entidad,
                descriptraslado = descriptraslado
            )

            mModel.actualizarEstadoAgenRepo(
                context = context,
                estadoAgenRepo = estadoAgenRepo
            )
        }
    }

    @Subscribe
    fun onEventEstadoAgenRepo(event: BasicEvent){
        if (mView != null && event != null){
            mView?.mostrarMsj(event.msj!!)
        }
    }
}