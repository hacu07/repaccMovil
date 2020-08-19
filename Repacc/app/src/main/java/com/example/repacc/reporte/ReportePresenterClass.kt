package com.example.repacc.reporte

import android.content.Context
import android.net.Uri
import com.example.repacc.pojo.Reporte
import com.example.repacc.reporte.model.ReporteModel
import com.example.repacc.reporte.model.ReporteModelClass
import com.example.repacc.reporte.view.ReporteActivity
import com.example.repacc.reporte.view.ReporteView
import com.example.repacc.reportes.events.ReporteEvent
import com.example.repacc.util.BasicEvent
import com.example.repacc.util.Util
import com.example.repacc.vehiculoAgregar.events.TipoEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class ReportePresenterClass: ReportePresenter {

    private var mView : ReporteView? = null
    private lateinit var mModel: ReporteModel

    constructor(mView: ReporteActivity){
        this.mView = mView
        mModel = ReporteModelClass()
    }

    override fun onCreate() {
        if(mView != null){
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        mView = null
    }

    override fun obtenerServicios(context: Context) {
        if (mView != null){
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.obtenerServicios(context)
        }
    }

    override fun registrarReporte(
        context: Context,
        reporte: Reporte,
        mPhotoSelectedUri: Uri
    ) {
        if (mView != null){
            mView?.mostrarProgreso(true)
            mView?.habilitarElementos(false)
            mModel.registrarReporte(context,reporte,mPhotoSelectedUri)
        }
    }

    @Subscribe
    fun onServiciosEventListener(event: TipoEvent){
        if (mView != null){
            when(event.typeEvent){
                Util.SUCCESS ->{
                    mView?.mostrarProgreso(false)
                    mView?.habilitarElementos(true)
                    mView?.cargarServicios(event.content!!)
                }
                else ->{
                    mView?.mostrarProgreso(false)
                    mView?.mostrarMsj(event.msj!!)
                }
            }
        }
    }

    @Subscribe
    fun onReporteEventListener(event: ReporteEvent){
        if (mView != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)
            when(event.typeEvent){
                Util.SUCCESS ->{
                    mView?.mostrarDialogo(event.msj!!)
                }
                else -> {
                    mView?.mostrarMsj(event.msj!!)
                }
            }
        }
    }
}