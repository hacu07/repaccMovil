package com.example.repacc.vehiculoAgregar.model

import android.content.Context
import com.example.repacc.pojo.Vehiculo
import com.example.repacc.util.BasicCallback
import com.example.repacc.util.BasicEvent
import com.example.repacc.vehiculoAgregar.events.MarcaEvent
import com.example.repacc.vehiculoAgregar.events.ModeloEvent
import com.example.repacc.vehiculoAgregar.events.TipoEvent
import com.example.repacc.vehiculoAgregar.model.DAO.DAO
import org.greenrobot.eventbus.EventBus

class VehiculosAgregarModelClass : VehiculoAgregarModel {

    lateinit var mDAO : DAO

    constructor(){
        mDAO = DAO()
    }

    override fun obtenerTipos(context: Context) {
        mDAO?.obtenerTipos(context, object: BasicCallback{
            override fun response(event: Any) {
                EventBus.getDefault().post(event as TipoEvent)
            }
        })
    }

    override fun obtenerMarcas(context: Context, idTipo: String) {
        mDAO?.obtenerMarcas(context,idTipo,object: BasicCallback{
            override fun response(event: Any) {
                EventBus.getDefault().post(event as MarcaEvent)
            }
        })
    }

    override fun obtenerModelos(context: Context, idMarca: String) {
        mDAO?.obtenerModelos(context,idMarca,object: BasicCallback{
            override fun response(event: Any) {
                EventBus.getDefault().post(event as ModeloEvent)
            }
        })
    }

    override fun registroVehiculo(context: Context, vehiculo: Vehiculo) {
        mDAO?.registroVehiculo(context,vehiculo,object: BasicCallback{
            override fun response(event: Any) {
                EventBus.getDefault().post(event as BasicEvent)
            }
        })
    }
}