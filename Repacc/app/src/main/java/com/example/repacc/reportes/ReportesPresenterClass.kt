package com.example.repacc.reportes

import android.content.Context
import com.example.repacc.R
import com.example.repacc.reportes.events.ReporteEvent
import com.example.repacc.reportes.events.ReportesEvent
import com.example.repacc.reportes.model.ReportesModel
import com.example.repacc.reportes.model.ReportesModelClass
import com.example.repacc.reportes.view.ReportesActivity
import com.example.repacc.reportes.view.ReportesView
import com.example.repacc.util.Constantes
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ReportesPresenterClass: ReportesPresenter {

    private var mView: ReportesView? = null
    private lateinit var mModel: ReportesModel

    constructor(mView: ReportesActivity){
        this.mView = mView
        mModel = ReportesModelClass()
    }

    override fun obtenerReporte(context: Context, idReporte: String) {
        mView.let {
            it?.mostrarProgreso(true)
            it?.habilitarElementos(false)
            mModel.obtenerReporte(
                context,
                idReporte
            )
        }
    }

    override fun obtenerListaReportes(context: Context) {
        mView.let {
            if (Constantes.config!!.usuario!!.munNotif != null){
                it?.mostrarProgreso(true)
                it?.habilitarElementos(false)
                mModel.obtenerListaReportes(
                    context,
                    Constantes.config!!.usuario!!.munNotif!!._id
                )
            }else{
                it?.mostrarMsj(context.getString(R.string.actualizar_perfil))
            }
        }
    }

    override fun onCreate() {
        mView.let {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    @Subscribe
    fun onEventReporteListener(event: ReporteEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)
            when(event.typeEvent){
                Util.SUCCESS -> {
                    mView?.mostrarReporte(event.content!!)
                }else -> {
                    mView?.mostrarMsj(event.msj!!)
                }
            }
        }
    }

    @Subscribe
    fun onEventReportesListener(event: ReportesEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)
            when(event.typeEvent){
                Util.SUCCESS -> {
                    mView?.cargarReportes(event.content!!)
                }else -> {
                mView?.mostrarMsj(event.msj!!)
                }
            }
        }
    }
}