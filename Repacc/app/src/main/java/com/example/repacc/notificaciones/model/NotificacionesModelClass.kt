package com.example.repacc.notificaciones.model

import android.content.Context
import com.example.repacc.notificaciones.model.DAO.DAO
import com.example.repacc.pojo.Reporte
import com.example.repacc.reportes.events.ReporteEvent
import com.example.repacc.util.BasicCallback
import org.greenrobot.eventbus.EventBus

class NotificacionesModelClass : NotificacionesModel {

    private lateinit var dao: DAO

    constructor(){
        dao = DAO()
    }

    override fun obtenerReporte(context: Context, reporte: Reporte) {
        dao.obtenerReporte(context,reporte, object: BasicCallback{
            override fun response(event: Any) {
                postReporte(event as ReporteEvent)
            }
        })
    }

    fun postReporte(reporteEvent: ReporteEvent) {
        EventBus.getDefault().post(reporteEvent)
    }
}