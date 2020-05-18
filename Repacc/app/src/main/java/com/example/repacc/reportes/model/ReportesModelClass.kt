package com.example.repacc.reportes.model

import android.content.Context
import com.example.repacc.reportes.events.ReporteEvent
import com.example.repacc.reportes.events.ReportesEvent
import com.example.repacc.reportes.model.DAO.DAO
import com.example.repacc.util.BasicCallback
import org.greenrobot.eventbus.EventBus

class ReportesModelClass : ReportesModel {

    private lateinit var mDAO : DAO

    constructor(){
        mDAO = DAO()
    }

    override fun obtenerReporte(context: Context, idReporte: String) {
        mDAO.obtenerReporte(context, idReporte, object: BasicCallback{
            override fun response(event: Any) {
                postReporte(event as ReporteEvent)
            }
        })
    }

    fun postReporte(reporteEvent: ReporteEvent) {
        EventBus.getDefault().post(reporteEvent)
    }

    override fun obtenerListaReportes(context: Context, idMunicipio: String) {
        mDAO.obtenerListaReportes(context, idMunicipio, object: BasicCallback{
            override fun response(event: Any) {
                postListaReportes(event as ReportesEvent)
            }
        })
    }

    fun postListaReportes(reportesEvent: ReportesEvent) {
        EventBus.getDefault().post(reportesEvent)
    }
}