package com.example.repacc.vehiculo.model

import android.content.Context
import com.example.repacc.util.BasicCallback
import com.example.repacc.vehiculo.events.VehiculoEvent
import com.example.repacc.vehiculo.model.DAO.DAO
import org.greenrobot.eventbus.EventBus

class VehiculoModelClass: VehiculoModel {

    private var mDAO: DAO

    constructor(){
        mDAO = DAO()
    }

    override fun obtenerVehiculos(context: Context) {
        mDAO.obtenerVehiculos(context, object : BasicCallback {
            override fun response(event: Any) {
                EventBus.getDefault().post(event as VehiculoEvent)
            }
        })
    }
}