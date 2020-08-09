package com.example.repacc.reporteDetalle.model

import android.content.Context
import com.example.repacc.pojo.Auxiliares.EstadoAgenRepo
import com.example.repacc.pojo.Reporte
import com.example.repacc.reporteDetalle.EstadoEvent
import com.example.repacc.reporteDetalle.model.DAO.DAO
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import org.greenrobot.eventbus.EventBus

class ReporteDetalleModelClass : ReporteDetalleModel {

    private val mDao  = DAO()

    override fun actualizarEstadoReporte(context: Context, reporte: Reporte) {
        mDao.actualizarEstadoReporte(context,reporte, object: BasicCallback{
            override fun response(event: Any) {
                postActEstd(event as BasicEvent)
            }
        })
    }

    fun postActEstd(basicEvent: BasicEvent) {
        EventBus.getDefault().post(basicEvent)
    }

    override fun obtenerEstadosServicios(context: Context) {
        mDao.obtenerEstadosServicios(context, object: BasicCallback{
            override fun response(event: Any) {
                postEstados(event as EstadoEvent)
            }
        })
    }

    fun postEstados(estadoEvent: EstadoEvent) {
        EventBus.getDefault().post(estadoEvent)
    }

    override fun actualizarEstadoAgenRepo(context: Context, estadoAgenRepo: EstadoAgenRepo) {
        mDao.actualizarEstadoAgenRepo(context, estadoAgenRepo, object: BasicCallback{
            override fun response(event: Any) {
                postEstAgenRepo(event as BasicEvent)
            }
        })
    }

    fun postEstAgenRepo(basicEvent: BasicEvent) {
        EventBus.getDefault().post(basicEvent)
    }
}