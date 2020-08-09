package com.example.repacc.notificaciones

import android.content.Context
import com.example.repacc.notificaciones.model.NotificacionesModel
import com.example.repacc.notificaciones.model.NotificacionesModelClass
import com.example.repacc.notificaciones.view.NotificacionesActivity
import com.example.repacc.notificaciones.view.NotificacionesView
import com.example.repacc.pojo.Reporte
import com.example.repacc.reportes.events.ReporteEvent
import com.example.repacc.util.Util
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class NotificacionesPresenterClass : NotificacionesPresenter {

    private var mView : NotificacionesView? = null
    private lateinit var mModel : NotificacionesModel

    constructor(mView : NotificacionesActivity){
        this.mView = mView
        mModel = NotificacionesModelClass()
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

    override fun obtenerReporte(context: Context, reporte: Reporte) {
        mView.let {
            it?.habilitarElementos(false)
            mModel.obtenerReporte(context,reporte)
        }
    }

    @Subscribe
    fun onEventReporteListener(event : ReporteEvent){
        if (event != null){
            mView?.habilitarElementos(true)

            when(event.typeEvent){
                Util.SUCCESS -> mView?.irDetalleReporte(event.content!!)
                else -> mView?.mostrarMsj(event.msj!!)
            }
        }
    }
}