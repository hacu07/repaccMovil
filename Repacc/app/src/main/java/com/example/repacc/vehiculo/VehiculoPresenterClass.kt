package com.example.repacc.vehiculo

import android.content.Context
import com.example.repacc.util.Util
import com.example.repacc.vehiculo.events.VehiculoEvent
import com.example.repacc.vehiculo.model.VehiculoModel
import com.example.repacc.vehiculo.model.VehiculoModelClass
import com.example.repacc.vehiculo.view.VehiculoActivity
import com.example.repacc.vehiculo.view.VehiculoView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class VehiculoPresenterClass: VehiculoPresenter {

    private var mView: VehiculoView? = null
    private lateinit var mModel: VehiculoModel

    constructor(mView: VehiculoActivity){
        this.mView = mView
        mModel = VehiculoModelClass()
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

    override fun obtenerVehiculos(context: Context) {
        if (mView != null){
            mView?.mostrarProgreso(true)
            mModel.obtenerVehiculos(context)
        }
    }

    @Subscribe
    fun onVehiculosEventListener(event: VehiculoEvent){
        if (event != null){
            mView?.mostrarProgreso(false)
            mView?.habilitarElementos(true)

            when(event.typeEvent){
                Util.SUCCESS -> mView?.cargarVehiculos(event.content!!)
                else -> mView?.mostrarMsj(event.msj!!)
            }
        }
    }

}