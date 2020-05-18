package com.example.repacc.reporte.model

import android.content.Context
import com.example.repacc.pojo.Reporte
import com.example.repacc.reporte.model.DAO.DAO
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import com.example.repacc.vehiculoAgregar.events.TipoEvent
import org.greenrobot.eventbus.EventBus

class ReporteModelClass: ReporteModel {

    lateinit var mDAO: DAO

    constructor(){
        mDAO = DAO()
    }

    override fun obtenerServicios(context: Context) {
        mDAO.obtenerServicios(context, object: BasicCallback{
            override fun response(event: Any) {
                postServices(event as TipoEvent)
            }
        })
    }

    fun postServices(tipoEvent: TipoEvent) {
        EventBus.getDefault().post(tipoEvent)
    }

    override fun registrarReporte(context: Context, reporte: Reporte) {
        mDAO.registrarReporte(context,reporte,object: BasicCallback{
            override fun response(event: Any) {
                postReporte(event as BasicEvent)
            }
        })
    }

    fun postReporte(basicEvent: BasicEvent) {
        EventBus.getDefault().post(basicEvent)
    }
}